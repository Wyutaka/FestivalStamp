package com.example.nakatsuka.newgit.mainAction.controller.beacon

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.util.Log
import com.example.nakatsuka.newgit.mainAction.model.beacon.MyBeaconData
import org.altbeacon.beacon.*
import java.util.*


class BeaconController(val parentContext:Context) : BeaconConsumer {
    //一定時間単位でBeacon位置を収集するリスト
    val myBeaconDataList: MutableList<MyBeaconData> = mutableListOf()
    private val handler: Handler = Handler()

    private val TAG = this.javaClass.simpleName

    private val mRegion = Region("APIAndBeaconModuleRegion", Identifier.parse("0f0ba8e1-f98a-4f59-af70-91308c6620b5"), null, null)
    private var startTime = Date().time

    object BeaconUtil {
        const val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
    }


    override fun getApplicationContext(): Context = parentContext.applicationContext
    override fun unbindService(p0: ServiceConnection?) = parentContext.unbindService(p0)
    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean = parentContext.bindService(p0, p1, p2)

    private lateinit var mBeaconManager: BeaconManager

    fun onCreate() {
        mBeaconManager = BeaconManager.getInstanceForApplication(parentContext)

        // iBeaconの受信設定：iBeaconのフォーマットを登録する
        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconUtil.IBEACON_FORMAT))

        mBeaconManager.bind(this)
    }

    override fun onBeaconServiceConnect() {
        Log.i(TAG, "onBeaconServiceConnect called")

        // レンジングのイベント設定

        mBeaconManager.addRangeNotifier { beaconList, _ ->

            Log.d(TAG,"beaconList.size = ${beaconList.size}")

            if (Date().time - startTime < 3000) {
                myBeaconDataList.addAll(beaconList.asSequence().map { MyBeaconData(it.id2.toInt(), it.rssi) })
            } else {
                Log.d(TAG, "time passed: ${Date().time - startTime}, count: ${myBeaconDataList.size}")
                mBeaconManager.stopRangingBeaconsInRegion(mRegion)
                //全ループ終了後に非同期処理を行う
                handler.post {
                    onBeaconDataIsUpdated?.invoke(myBeaconDataList)
                    myBeaconDataList.clear()
                }
            }
        }

        /*
        *旧版
        *
        mBeaconManager.addRangeNotifier(object : RangeNotifier {
            override fun didRangeBeaconsInRegion(beaconList: MutableCollection<Beacon>, region: Region) {

                Log.d(TAG, "didRangeBeaconInRegion called")

                if (Date().time - startTime < 3000) {
                    beaconList.forEach {
                        //var savedata = "major:"+beacon.id2+" minor:"+beacon.id3+" rssi:"+beacon.rssi
                        var saveMajor = it.id2.toString()
                        var saverssi = Integer.toString(it.rssi)
                        Log.d(TAG, saveMajor + ":" + saverssi)

                        myBeaconDataList.add(MyBeaconData(saveMajor.toInt(), it.rssi))
                    }
                } else {
                    Log.d(TAG, "time passed: ${Date().time - startTime}, count: ${myBeaconDataList.size}")
                    mBeaconManager.stopRangingBeaconsInRegion(mRegion)

                }
                    //全ループ終了後に非同期処理を行う
                    handler.post {
                        onBeaconDataIsUpdated?.invoke(myBeaconDataList)
                        myBeaconDataList.clear()
                    }
                }
            })
            */
    }



    //コールバックメソッド
    var onBeaconDataIsUpdated :((beaconListModel: MutableCollection< MyBeaconData >) -> Unit)? = null

    fun rangeBeacon(overWriteFun:(MutableCollection<MyBeaconData>)->Unit) {
        Log.i(TAG, "rangeBeacon Called")
        mBeaconManager.startRangingBeaconsInRegion(mRegion)
        startTime = Date().time
        onBeaconDataIsUpdated = overWriteFun
    }
}
