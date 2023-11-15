package com.laikaalper.florist_app.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.laikaalper.florist_app.R
import com.laikaalper.florist_app.common.viewBinding
import com.laikaalper.florist_app.databinding.FragmentPaymentBinding
import com.laikaalper.florist_app.ui.MainActivity


class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val binding by viewBinding(FragmentPaymentBinding::bind)
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottom_nav);
        bottomNavigationView?.setVisibility(View.GONE);

        with(binding) {
            btnPay.setOnClickListener {
                val address = etAddress.text.toString()
                val name = etNameSurname.text.toString()
                val cardNumber = etCardNumber.text.toString()
                val cardDate = etCardDate.text.toString()
                val cvc = etCvc.text.toString()

                if (cardNumber.length == 16 && cardDate.isNotEmpty() && cvc.isNotEmpty() && address.isNotEmpty() && name.isNotEmpty()) {
                    MainActivity.navigate(R.id.push_success_fragment)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Tüm boşlukları doldurun ve kart numarasının 16 karakter uzunluğunda olduğundan emin olun",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            btnCancel.setOnClickListener {
                findNavController().navigateUp()
            }

        }
    }

}