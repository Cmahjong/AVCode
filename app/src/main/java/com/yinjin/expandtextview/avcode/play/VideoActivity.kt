package com.yinjin.expandtextview.avcode.play

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import com.yinjin.expandtextview.avcode.R
import com.yinjin.player.util.MyLog
import com.yinjin.player.util.WlTimeUtil
import com.yinjin.player.wlplayer.WlPlayer
import com.yinjin.player.wlplayer.WlTimeBean

import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity() {
    /** 播放器 */
    val yjPlayer: WlPlayer by lazy { WlPlayer() }
    val pathurl: String by lazy { intent.extras.getString("url") }
    var ispause: Boolean = false
    var isSeek: Boolean = false
    var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_video)
        iv_pause.setOnClickListener { pause() }
        iv_cutimg.setOnClickListener { cutImg() }
        yjPlayer.setOnlyMusic(false)
        yjPlayer.setDataSource(pathurl)
        yjPlayer.isOnlySoft = true
        yjPlayer.setWlGlSurfaceView(surfaceview)
        yjPlayer.setWlOnErrorListener { code, msg ->
            MyLog.d("code:$code,msg:$msg")
            val message = Message.obtain()
            message.obj = msg
            message.what = 3
            handler.sendMessage(message)
        }
        yjPlayer.setWlOnPreparedListener {
            MyLog.d("starting......")
            yjPlayer.start()
        }
        yjPlayer.setWlOnLoadListener {
            MyLog.d("loading ......>>>   " + it)
            val message = Message.obtain()
            message.what = 1
            message.obj = it
            handler.sendMessage(message)
        }
        yjPlayer.setWlOnInfoListener {
            val message = Message.obtain()
            message.what = 2
            message.obj = it
            MyLog.d("nowTime is " + it.currt_secds)
            handler.sendMessage(message)
        }
        yjPlayer.setWlOnCompleteListener {
            MyLog.d("complete .....................")
            yjPlayer.stop(true)
        }
        yjPlayer.setWlOnCutVideoImgListener {
            val message = Message.obtain()
            message.what = 4
            message.obj = it
            handler.sendMessage(message)
        }
        yjPlayer.prepared()
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                position = yjPlayer.duration * progress / 100
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeek = true
                position?.let { yjPlayer.seek(it) }
            }
        })
    }

    @SuppressLint("HandlerLeak")
    private var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                val load = msg.obj as Boolean
                if (load) {
                    pb_loading.visibility = View.VISIBLE
                } else {
                    if (ly_action.visibility != View.VISIBLE) {
                        ly_action.visibility = View.VISIBLE
                        iv_cutimg.visibility = View.VISIBLE
                    }
                    pb_loading.visibility = View.GONE
                }
            } else if (msg.what == 2) {
                val wlTimeBean = msg.obj as WlTimeBean
                if (wlTimeBean.total_secds > 0) {
                    seekbar.visibility = View.VISIBLE
                    if (isSeek) {
                        seekbar.progress = position!! * 100 / wlTimeBean.total_secds
                        isSeek = false
                    } else {
                        seekbar.progress = wlTimeBean.currt_secds * 100 / wlTimeBean.total_secds
                    }
                    tv_time.text = WlTimeUtil.secdsToDateFormat(wlTimeBean.total_secds) + "/" + WlTimeUtil.secdsToDateFormat(wlTimeBean.currt_secds)
                } else {
                    seekbar.visibility = View.GONE
                    tv_time.text = WlTimeUtil.secdsToDateFormat(wlTimeBean.currt_secds)
                }
            } else if (msg.what == 3) {
                val err = msg.obj as String
                Toast.makeText(this@VideoActivity, err, Toast.LENGTH_SHORT).show()
            } else if (msg.what == 4) {
                val bitmap = msg.obj as Bitmap
                if (bitmap != null) {
                    iv_show_img.visibility = View.VISIBLE
                    iv_show_img.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (yjPlayer != null) {
            yjPlayer.stop(true)
        }
        this.finish()

    }

    fun pause() {
        if (yjPlayer != null) {
            ispause = !ispause
            if (ispause) {
                yjPlayer.pause()
                iv_pause.setImageResource(R.mipmap.ic_play_play)
            } else {
                yjPlayer.resume()
                iv_pause.setImageResource(R.mipmap.ic_play_pause)
            }
        }

    }

    fun cutImg() {
        if (yjPlayer != null) {
            yjPlayer.cutVideoImg()
        }
    }
}
