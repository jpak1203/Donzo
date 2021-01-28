package com.appsbyjpak.donzo.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.appsbyjpak.donzo.Activities.MainActivity
import com.appsbyjpak.donzo.R
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executor

class LoginTabFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var forgotPassword: TextView
    lateinit var loginButton: Button
    lateinit var userAuthFailed: TextView
    var v = 0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_login_tab, container, false)

        mAuth = FirebaseAuth.getInstance()

        email = root.findViewById(R.id.login_email_input)
        password = root.findViewById(R.id.login_password_input)
        forgotPassword = root.findViewById(R.id.login_forgot_password)
        loginButton = root.findViewById(R.id.login_button)
        userAuthFailed = root.findViewById(R.id.user_auth_failed)

        userAuthFailed.visibility = TextView.GONE

        email.translationY = 300f
        password.translationY = 300f
        forgotPassword.translationY = 300f
        loginButton.translationY = 300f

        email.alpha = v
        password.alpha = v
        forgotPassword.alpha = v
        loginButton.alpha = v

        email.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(300).start()
        password.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(500).start()
        forgotPassword.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(500).start()
        loginButton.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(700).start()

        submitLogin()
        confirmLogin()

        return root
    }

    private fun login() {
        if (!validateForm()) return

        mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("user_auth", "signInWithEmail:success")
                    val myIntent = Intent(context, MainActivity::class.java)
                    startActivity(myIntent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("user_auth", "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                    userAuthFailed.visibility = TextView.VISIBLE
                }
            }
    }

    private fun submitLogin() {
        loginButton.setOnClickListener {
            login()
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        val emailText = email.text.toString()

        if (TextUtils.isEmpty(email.text)) {
            email.error = "This field cannot be blank"
            valid = false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.error = "Invalid email"
            valid = false
        }

        if (TextUtils.isEmpty(password.text)) {
            password.error = "This field cannot be blank"
            valid = false
        }

        return valid
    }

    private fun confirmLogin() {
        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (TextUtils.isEmpty(p0)) {
                    loginButton.setBackgroundResource(R.drawable.user_auth_button_bg)
                    loginButton.setTextColor(ContextCompat.getColor(context!!, R.color.light))
                    loginButton.isClickable = false
                } else {
                    userAuthFailed.visibility = TextView.GONE
                    loginButton.setBackgroundResource(R.drawable.user_auth_button_enabled)
                    loginButton.setTextColor(ContextCompat.getColor(context!!, R.color.dark))
                    loginButton.isClickable = true
                }
            }
        })

        password.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                login()
                return@OnKeyListener true
            }
            false
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginTabFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            LoginTabFragment().apply {
                }
    }
}