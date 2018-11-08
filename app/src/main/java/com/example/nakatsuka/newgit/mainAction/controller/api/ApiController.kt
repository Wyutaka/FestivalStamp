package com.example.nakatsuka.newgit.mainAction.controller.api

import android.util.Log
import com.example.nakatsuka.newgit.mainAction.controller.beacon.BeaconController
import com.example.nakatsuka.newgit.mainAction.model.api.*
import com.example.nakatsuka.newgit.mainAction.model.beacon.MyBeaconData
import com.example.nakatsuka.newgit.mainAction.service.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiController {

    companion object {
        private val TAG get() = ApiController::class.java.simpleName
    }

    fun getRegulation(onResponse: (response: Response<RuleResponse>) -> Unit){
        APIClient.instance.rule().enqueue(object :Callback<RuleResponse>{
            override fun onResponse(call: Call<RuleResponse>, response: Response<RuleResponse>) {
                onResponse(response)
                if (response.code() == 200) {
                    Log.d(TAG, "ruleText=${response.body()?.ruleText}")
                } else {
                    Log.e(TAG, "${response.code()}, ${response.body()}, ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<RuleResponse>, t: Throwable) {
                Log.e(TAG, t.localizedMessage, t)
            }
        })
    }

    fun registerUser(userName: String, device: String, version: String, onResponse: (response: Response<UserResponse>) -> Unit) {
        val request = UserRequest(userName, device, version)
        APIClient.instance.user(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onResponse(response)
                if (response.code() == 200) {
                    Log.d(TAG, "${response.body()}")
                } else {
                    Log.e(TAG, "${response.code()}, ${response.body()}, ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, t.localizedMessage, t)
            }
        })
    }

    /**
     * 旧版requestImage.BeaconControllerを引数にとって渡しているのが問題点だった。
     * */
    fun requestImage(uuid: String, quizCode: Int, mBeaconController: BeaconController, onResponse: (response: Response<ImageResponse>) -> Unit) {
        val beaconList = mBeaconController.myBeaconDataList
        val request = ImageRequest(quizCode, beaconList.map { it.major })

        APIClient.instance.image(request, uuid).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                onResponse(response)
                if (response.isSuccessful) {
                    val judged = response.body()
                    Log.d(TAG, "quizCode=${judged?.quizCode}, message=${judged?.messageId}, isSend=${judged?.isSend}, url=${judged?.imageURL}")
                } else {
                    Log.e(TAG, "${response.code()}, ${response.body()}, ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Log.e(TAG, t.localizedMessage, t)
            }
        })

    }
    /**
     * beaconManagerを直接渡すのではなく、beaconListだけを渡す仕様に変更した。
     * */
    fun requestImage(uuid: String, quizCode: Int, beaconList:MutableList<MyBeaconData>, onResponse: (response: Response<ImageResponse>) -> Unit) {

        val request = ImageRequest(quizCode, beaconList.map { it.major })
        Log.d(TAG,"BeaconList = ")

        //APIClientで、上で作ったrequestを用いてAPI通信を行う
        APIClient.instance.image(request, uuid).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                onResponse(response)
                if (response.isSuccessful) {
                    val judged = response.body()
                    Log.d(TAG, "quizCode=${judged?.quizCode}, message=${judged?.messageId}, isSend=${judged?.isSend}, url=${judged?.imageURL}")
                } else {
                    Log.e(TAG, "${response.code()}, ${response.body()}, ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Log.e(TAG, t.localizedMessage, t)
            }
        })
    }

    fun judgeAnswer(uuid: String, quizCode: Int, answer: String, onResponse: (response: Response<AnswerResponse>) -> Unit) {
        val request = AnswerRequest(quizCode, answer)

        APIClient.instance.answer(request, uuid).enqueue(object : Callback<AnswerResponse> {
            override fun onResponse(call: Call<AnswerResponse>, response: Response<AnswerResponse>) {
                onResponse(response)
                if (response.isSuccessful) {
                    val judged = response.body()
                    Log.d(TAG, "quizCode=${judged?.quizCode}, isCorrect=${judged?.isCorrect}")
                } else {
                    Log.e(TAG, "${response.code()}, ${response.body()}, ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<AnswerResponse>, t: Throwable) {
                Log.e(TAG, t.localizedMessage, t)
            }
        })
    }

    fun requestGoal(uuid: String, onResponse: (response: Response<GoalResponse>) -> Unit) {
        APIClient.instance.goal(uuid).enqueue(object : Callback<GoalResponse> {
            override fun onResponse(call: Call<GoalResponse>, response: Response<GoalResponse>) {
                onResponse(response)
                if (response.isSuccessful) {
                    val judged = response.body()
                    Log.d(TAG, "isGoal=${judged?.isGoal}")
                } else {
                    Log.e(TAG, "${response.code()}, ${response.body()}, ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GoalResponse>, t: Throwable) {
                Log.e(TAG, t.localizedMessage, t)
            }
        })
    }

}
