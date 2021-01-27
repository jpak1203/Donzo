package com.appsbyjpak.donzo.Fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.appsbyjpak.donzo.Activities.MainActivity
import com.appsbyjpak.donzo.R
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignupTabFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var confirm: EditText
    lateinit var signupButton: Button
    var v = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_signup_tab, container, false)

        mAuth = FirebaseAuth.getInstance()

        email = root.findViewById(R.id.signup_email_input)
        password = root.findViewById(R.id.signup_password_input)
        confirm = root.findViewById(R.id.signup_confirm_input)
        signupButton = root.findViewById(R.id.signup_button)

        email.translationY = 300f
        password.translationY = 300f
        confirm.translationY = 300f
        signupButton.translationY = 300f

        email.alpha = v
        password.alpha = v
        confirm.alpha = v
        signupButton.alpha = v

        email.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(300).start()
        password.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(500).start()
        confirm.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(500).start()
        signupButton.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(700).start()

        submitSignUp()
        confirmPassword()

        return root
    }

    private fun createAccount() {
        if (!validateForm()) return

        val email = email.text.toString()
        val password = password.text.toString()

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("Authentication", "createUserWithEmail:success")
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("Authentication", "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    context, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun submitSignUp() {
        signupButton.setOnClickListener {
            createAccount()
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        val emailText = email.text.toString()
        val passwordText = password.text.toString()

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

        if (!isValidPassword(passwordText) && passwordText.length < 6) {
            password.error = "Invalid password: Must contain 1 capital letter, 1 number, and 1 symbol (@,\$,%,&,#,)"
            valid = false
        }

        if (password.text.toString() != confirm.text.toString() || TextUtils.isEmpty(
                confirm.text
            )) {
            confirm.error = "Does not match password"
            valid = false
        }

        return valid
    }

    private fun confirmPassword() {
        confirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString() != password.text.toString()) {
                    signupButton.setBackgroundResource(R.drawable.user_auth_button_bg)
                    signupButton.setTextColor(ContextCompat.getColor(context!!, R.color.light))
                    signupButton.isClickable = false
                } else {
                    signupButton.setBackgroundResource(R.drawable.user_auth_button_enabled)
                    signupButton.setTextColor(ContextCompat.getColor(context!!, R.color.dark))
                    signupButton.isClickable = true
                }
            }
        })

        confirm.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                createAccount()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher

        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)

        return matcher.matches()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupTabFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SignupTabFragment().apply {
            }
    }
}