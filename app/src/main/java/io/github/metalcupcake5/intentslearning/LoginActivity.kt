package io.github.metalcupcake5.intentslearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
        val REQUEST_LOGIN_INFO = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Backendless.initApp(this, Constants.APP_ID, Constants.API_KEY );

        button_login_login.setOnClickListener {
            val username = editText_login_username.text.toString()
            val password = editText_login_password.text.toString()

            Backendless.UserService.login(username, password, object : AsyncCallback<BackendlessUser> {
                override fun handleResponse(response: BackendlessUser?) {
                    Toast.makeText(this@LoginActivity, "${response?.userId} has logged in", Toast.LENGTH_SHORT).show()

                    val gradeListIntent = Intent(this@LoginActivity, GradeListActivity::class.java)
                    startActivity(gradeListIntent)
                    finish()
                }

                override fun handleFault(fault: BackendlessFault?) {
                    Log.d(GradeListActivity.TAG, "handleFault: " + fault?.message)
                    Toast.makeText(this@LoginActivity, "Something went wrong. Check the logs", Toast.LENGTH_SHORT).show()
                }
            })

        }

        //make onclicklistener for signup button
        button_login_signup.setOnClickListener {
            //extact current text from username box
            val username = editText_login_username.text.toString()
            val password = editText_login_password.text.toString()
            //create an intent to launch registration activity
            // FileName::class.java gives you access to class location
            val registrationIntent = Intent(this, RegistrationActivity::class.java).apply {
                //store username in an "extra" in the intent
                putExtra(EXTRA_USERNAME, username)
                putExtra(EXTRA_PASSWORD, password)
            }

            //launch new activity
            //startActivity(registrationIntent)
            startActivityForResult(registrationIntent, REQUEST_LOGIN_INFO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_LOGIN_INFO) {
            if (resultCode == RESULT_OK) {
                editText_login_username.setText(data?.getStringExtra(RegistrationActivity.EXTRA_USERNAME))
                editText_login_password.setText(data?.getStringExtra(RegistrationActivity.EXTRA_PASSWORD))
            }
        }
    }
}