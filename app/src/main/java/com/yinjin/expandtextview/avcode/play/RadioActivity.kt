package com.yinjin.expandtextview.avcode.play

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.yinjin.expandtextview.avcode.R
import com.yinjin.expandtextview.avcode.play.servicehttp.bean.RadioLiveChannelBean
import com.yinjin.player.listener.*
import com.yinjin.player.util.WlTimeUtil
import com.yinjin.player.wlplayer.WlPlayer
import com.yinjin.player.wlplayer.WlTimeBean
import kotlinx.android.synthetic.main.activity_radio.*
import kotlinx.android.synthetic.main.include_common_text_title.*
import kotlinx.android.synthetic.main.include_common_text_title.view.*

class RadioActivity : AppCompatActivity() {
    var index = 0
   val wlPlayer: WlPlayer by lazy {
       WlPlayer()
   }
    val parcelableArrayExtra:ArrayList<RadioLiveChannelBean> by lazy { intent.getParcelableArrayListExtra<RadioLiveChannelBean>("url") }
    private var isPlay = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radio)
        val radioLiveChannelBean = parcelableArrayExtra[0]
        Glide.with(this).load(radioLiveChannelBean.img).into(siv_logo)
        playRadio(radioLiveChannelBean)
        wlPlayer.setWlOnPreparedListener(object : WlOnPreparedListener {
           override fun onPrepared() {
                wlPlayer.start()
                val message = Message.obtain()
                message.what = 4
                handler.sendMessage(message)
            }
        })
        wlPlayer.setWlOnLoadListener(object : WlOnLoadListener {
            override fun onLoad(load: Boolean) {
                val message = Message.obtain()
                message.what = 1
                message.obj = load
                handler.sendMessage(message)
            }
        })
        wlPlayer.setWlOnInfoListener(object : WlOnInfoListener {
            override fun onInfo(wlTimeBean: WlTimeBean) {
                val message = Message.obtain()
                message.what = 2
                message.obj = wlTimeBean
                handler.sendMessage(message)
            }
        })

        wlPlayer.setWlOnCompleteListener(object : WlOnCompleteListener {
            override fun onComplete() {
                isPlay = false
            }
        })

        wlPlayer.setWlOnStopListener(object : WlOnStopListener {
            override   fun onStop() {
                val message = Message.obtain()
                message.what = 3
                handler.sendMessage(message)
            }
        })

        wlPlayer.prepared()
        title_content.iv_back.setOnClickListener { onBackPressed() }
        iv_pre.setOnClickListener {
            if (wlPlayer != null && index > 0) {
                index--
                wlPlayer.stop(false)
            } else {
                Toast.makeText(this,"已经到第一项了",Toast.LENGTH_SHORT).show()
            } }
        iv_next.setOnClickListener {
            if (wlPlayer != null && index < parcelableArrayExtra.size - 1) {
                index++
                wlPlayer.stop(false)
            } else {
                Toast.makeText(this,"已经到最后一项了",Toast.LENGTH_SHORT).show()
            }
        }
        iv_play.setOnClickListener {
            if (wlPlayer != null) {
                isPlay = !isPlay
                if (isPlay) {
                    iv_play.setImageResource(R.mipmap.ic_play_pause)
                    wlPlayer.resume()
                } else {
                    iv_play.setImageResource(R.mipmap.ic_play_play)
                    wlPlayer.pause()
                }
            }
        }

    }

    @SuppressLint("HandlerLeak")
    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                val load = msg.obj as Boolean
                if (load) {
                    pb_load.setVisibility(View.VISIBLE)
                    iv_play.setVisibility(View.INVISIBLE)
                } else {
                    pb_load.setVisibility(View.INVISIBLE)
                    iv_play.setVisibility(View.VISIBLE)
                }
            } else if (msg.what == 2) {
                val wlTimeBean = msg.obj as WlTimeBean
                if (wlTimeBean.total_secds > 0) {
                    tv_time.setText(WlTimeUtil.secdsToDateFormat(wlTimeBean.total_secds) + "/" + WlTimeUtil.secdsToDateFormat(wlTimeBean.currt_secds))
                } else {
                    tv_time.setText(WlTimeUtil.secdsToDateFormat(wlTimeBean.currt_secds))
                }
            } else if (msg.what == 3) {
                playRadio(getPlayChannelBean())
            } else if (msg.what == 4) {
                iv_play.setImageResource(R.mipmap.ic_play_pause)
            }
        }
    }
    private fun playRadio(radioLiveChannelBean: RadioLiveChannelBean?) {
        if (radioLiveChannelBean != null && wlPlayer != null) {
            try {
                pb_load.visibility = View.VISIBLE
                iv_play.visibility = View.INVISIBLE
                wlPlayer.setDataSource(radioLiveChannelBean.streams[0].url)
                wlPlayer.setOnlyMusic(true)
                tv_live_name.text = radioLiveChannelBean.liveSectionName
                title = radioLiveChannelBean.name
                Glide.with(this).load(radioLiveChannelBean.img).into(siv_logo)
                tv_time.text = "00:00:00"
                wlPlayer.prepared()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(this,"不能再切换了",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPlayChannelBean(): RadioLiveChannelBean? {
        return if (index >= 0 && index < parcelableArrayExtra.size) {
            parcelableArrayExtra.get(index) as RadioLiveChannelBean
        } else null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
        if (wlPlayer != null) {
            wlPlayer.stop(true)
        }
    }
}
