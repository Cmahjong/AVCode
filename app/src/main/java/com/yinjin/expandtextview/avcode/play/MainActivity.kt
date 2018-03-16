package com.yinjin.expandtextview.avcode.play

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.yinjin.expandtextview.avcode.R
import com.yinjin.expandtextview.avcode.play.servicehttp.bean.PandaTvDataBean
import com.yinjin.expandtextview.avcode.play.servicehttp.bean.PandaTvLiveDataBean
import com.yinjin.expandtextview.avcode.play.servicehttp.bean.RadioLiveChannelBean
import com.yinjin.expandtextview.avcode.play.servicehttp.serviceapi.RadioApi
import com.yinjin.expandtextview.avcode.play.servicehttp.serviceapi.VideoApi
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        video.setOnClickListener {
            showProgressDialog("数据加载中...")
            getVideoList()
        }
        radio.setOnClickListener {
            showProgressDialog("数据加载中...")
            getToken()

        }

    }

    private fun getToken() {
        RadioApi.getInstance().getToken(object : Observer<Int> {
            override fun onError(e: Throwable) {

            }

            override fun onNext(t: Int) {
                getLiveByParam(t.toString(), "3225", 2, 1)
            }

            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }
        })
    }

    private fun getLiveByParam(token: String, channelPlaceId: String, limit: Int, offset: Int) {
        RadioApi.getInstance().getLiveByParam(token, channelPlaceId, limit, offset, object : Observer<ArrayList<RadioLiveChannelBean>> {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: ArrayList<RadioLiveChannelBean>) {
                val intent = Intent(this@MainActivity, RadioActivity::class.java)
                intent.putParcelableArrayListExtra("url", t)
                startActivity(intent)
            }
        })
    }

    private fun getVideoList() {
        VideoApi.getInstance().getVideList("lol", 1, 5, 1, "3.3.1.5978", object : Observer<PandaTvDataBean> {
            override fun onError(e: Throwable) {
                Toast.makeText(this@MainActivity, "加载失败...", Toast.LENGTH_SHORT).show()
            }

            override fun onNext(pandaTvDataBean: PandaTvDataBean) {
                getLiveUrl(pandaTvDataBean.items[1].id, "3.3.1.5978")
            }

            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }
        })
    }

    private fun getLiveUrl(roomId: String, version: String) {
        VideoApi.getInstance().getLiveUrl(roomId, version, object : Observer<PandaTvLiveDataBean> {
            override fun onComplete() {

            }

            override fun onNext(data: PandaTvLiveDataBean) {
                val pl = data.info.videoinfo.plflag.split("_")
                val url = "http://pl" + pl[pl.size - 1] + ".live.panda.tv/live_panda/" + data.info.videoinfo.room_key + "_mid.flv?sign=" + data.info.videoinfo.sign + "&time=" + data.info.videoinfo.ts
                val bundle = Bundle()
                bundle.putString("url", url)
                val intent = Intent(this@MainActivity, VideoActivity::class.java)
                intent.putExtra("url", url)
                startActivity(intent)
                dismissProgressDialog()
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                Toast.makeText(this@MainActivity, "加载失败...", Toast.LENGTH_SHORT).show()
            }
        })
    }


    /**
     * show a progress dialog with the given message.
     */
    fun showProgressDialog(msg: String) {
        try {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(this)
            }
            progressDialog!!.setMessage(msg)
            progressDialog!!.show()
        } catch (e: Exception) {
            progressDialog = null
        }

    }

    /**
     * show a progress dialog with the given message.
     */
    fun showProgressDialog(msg: String, cancelable: Boolean) {
        if (null == progressDialog) {
            progressDialog = ProgressDialog(this)
            progressDialog!!.setCancelable(cancelable)
            progressDialog!!.setCanceledOnTouchOutside(false)
        }

        if (!progressDialog!!.isShowing()) {
            if (!isFinishing) {
                progressDialog!!.show()
                progressDialog!!.setMessage(msg)
            }
        }
    }

    /**
     * close a progress if there is one.
     */
    fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing()) {
            progressDialog!!.dismiss()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //add 20160504 fanhl java.lang.IllegalArgumentException android.app.Dialog.dismissDialog
        progressDialog = null
    }
}
