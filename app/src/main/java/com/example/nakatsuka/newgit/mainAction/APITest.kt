package com.example.nakatsuka.newgit.mainAction

import android.os.Parcel
import android.os.Parcelable

class APITest() :Parcelable{
    private var button_number:Int = 0
    private var APIData:String = ""

    //isAPIはAPIが成功しているか判別するために用いる(デバッグのようなもの)
    private var isAPI:Boolean = false
    private var userName = ""
    private var device = ""
    private var UUID = ""

    constructor(parcel: Parcel) : this() {
        button_number = parcel.readInt()
        APIData = parcel.readString()
        isAPI = parcel.readByte() != 0.toByte()
        userName = parcel.readString()
        device = parcel.readString()
        UUID = parcel.readString()
    }

    fun setButtonNumber(buttonNumber: Int){
        this.button_number = buttonNumber
    }

    fun SetandPostUserJSON(userName:String,device:String,UUID:String){
        this.userName = userName
        this.device = device
        this.UUID = UUID
    }

    fun GetandPostUserJSON():String{
        return userName+device+UUID
    }

    //このメソッドにAPI側から答えをセット



    fun getAPIData():String{
        //APIからURLをもらうところを代用
        when(button_number){
            1 -> APIData = "https://pbs.twimg.com/profile_images/983595280850305024/-e7O7KaL_400x400.jpg"
            2 -> APIData = "https://yt3.ggpht.com/-phqR1pvakkM/AAAAAAAAAAI/AAAAAAAAAAA/ajOkOO-ItrU/s288-mo-c-c0xffffffff-rj-k-no/photo.jpg"
            3 -> APIData = "http://livedoor.blogimg.jp/na_seiji-virtual/imgs/1/f/1ff83a0b-s.jpg"
            4 -> APIData = "https://akari-mir.ai/wp-content/uploads/2018/03/Akari_iphonex.jpg"
            5 -> APIData = "https://youtuberlabo.com/wp-content/uploads/2018/01/SnapCrab_NoName_2018-1-13_20-47-28_No-00.png"
            6 -> APIData = "https://lohas.nicoseiga.jp/thumb/8165390i?1527286117"
        }
        //ここでAPIDataに文字列が入ったかどうか判別
        if(!(APIData.equals(""))){
            isAPI = true
        }
        return APIData
    }

    fun getIsAPI():Boolean{
        isAPI = true
        return isAPI
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(button_number)
        parcel.writeString(APIData)
        parcel.writeByte(if (isAPI) 1 else 0)
        parcel.writeString(userName)
        parcel.writeString(device)
        parcel.writeString(UUID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<APITest> {
        override fun createFromParcel(parcel: Parcel): APITest {
            return APITest(parcel)
        }

        override fun newArray(size: Int): Array<APITest?> {
            return arrayOfNulls(size)
        }
    }


}