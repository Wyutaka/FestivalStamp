package com.example.nakatsuka.newgit.mainAction.controller.api

import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.nakatsuka.newgit.mainAction.*
import com.example.nakatsuka.newgit.mainAction.model.api.*
import com.example.nakatsuka.newgit.mainAction.model.beacon.*
import com.example.nakatsuka.newgit.mainAction.service.APIClient
import retrofit2.*

class ApiController {

    var TAG = this.javaClass.simpleName

    fun getRegulation(activity: RuleActivity, agreeButton: Button, title: TextView, onResponse: (response: Response<RuleResponse>) -> Unit) {
        APIClient.instance.rule().enqueue(object : Callback<RuleResponse> {
            override fun onResponse(call: Call<RuleResponse>, response: Response<RuleResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<RuleResponse>, t: Throwable) {
                //テキストもろもろ消して最初の画面に戻ります
                agreeButton.visibility = View.INVISIBLE
                title.visibility = View.GONE
                AlertDialog.Builder(activity)
                        .setTitle("通信エラー")
                        .setMessage("通信状況を確認の上再度お試しください")
                        .setPositiveButton("OK") { _, _ ->
                            activity.jump()
                        }
                        .show()
            }
        })
    }

    fun registerUser(activity: ResistActivity, userName: String, device: String, version: String, onResponse: (response: Response<UserResponse>) -> Unit) {
        val request = UserRequest(userName, device, version)
        APIClient.instance.user(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onResponse(response)
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                AlertDialog.Builder(activity)
                        .setTitle("通信エラー")
                        .setMessage("通信状況を確認の上再度お試しください")
                        .setPositiveButton("OK") { _, _ ->
                        }
                        .show()
            }
        })
    }

    fun requestImage(uuid: String, quizCode: Int, beaconList: MutableList<MyBeaconData>, onResponse: (response: Response<ImageResponse>) -> Unit) {
        val request = ImageRequest(quizCode, beaconList.map { it.major })
        //APIClientで、上で作ったrequestを用いてAPI通信を行う
        APIClient.instance.image(request, uuid).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                onResponse(response)
            }
            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
            }
        })
    }
    fun requestImage(activity: FragmentActivity?, uuid: String, quizCode: Int, beaconList: MutableList<MyBeaconData>, onResponse: (response: Response<ImageResponse>) -> Unit) {

        val request = ImageRequest(quizCode, beaconList.map { it.major })
        Log.d(TAG, "beaconList = ")
        beaconList.forEach { Log.d(TAG, "${it.major} ") }
        Log.e("tatte", beaconList.toString())

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
                AlertDialog.Builder(activity!!)
                        .setTitle("通信エラー")
                        .setMessage("通信状況を確認の上再度お試しください")
                        .setPositiveButton("OK") { _, _ ->
                        }
                        .show()
            }
        })
    }

    fun judgeAnswer(activity: SecondActivity, uuid: String, quizCode: Int, answer: String, onResponse: (response: Response<AnswerResponse>) -> Unit) {
        val request = AnswerRequest(quizCode, answer)

        APIClient.instance.answer(request, uuid).enqueue(object : Callback<AnswerResponse> {
            override fun onResponse(call: Call<AnswerResponse>, response: Response<AnswerResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<AnswerResponse>, t: Throwable) {
                AlertDialog.Builder(activity)
                        .setTitle("通信エラー")
                        .setMessage("通信状況を確認の上再度お試しください")
                        .setPositiveButton("OK") { _, _ ->
                        }
                        .show()
            }
        })
    }

    fun requestGoal(uuid: String, onResponse: (response: Response<GoalResponse>) -> Unit) {
        APIClient.instance.goal(uuid).enqueue(object : Callback<GoalResponse> {
            override fun onResponse(call: Call<GoalResponse>, response: Response<GoalResponse>) {
                onResponse(response)
            }
            override fun onFailure(call: Call<GoalResponse>, t: Throwable) {
            }
        })
    }

}
