package com.example.cryptoapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ActivityCoinInfoListBinding
import com.example.cryptoapp.presentation.adapters.CoinInfoListAdapter
import kotlin.getValue

class CoinInfoListActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinInfoListActivityViewModel
    private lateinit var coinInfoListAdapter: CoinInfoListAdapter
    private val binding by lazy {
        ActivityCoinInfoListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpRV()
        viewModel = ViewModelProvider(this)[CoinInfoListActivityViewModel::class.java]
        viewModel.coinListInfo.observe(this) {
            coinInfoListAdapter.submitList(it)
        }
    }
    private fun isOnePlaneMode(): Boolean{
        return binding.coinInfoContainer == null
    }

    private fun launchFragmentMode(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.coin_info_container,fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun setUpRV(){
        binding.rvCoinPriceList.apply {
            coinInfoListAdapter = CoinInfoListAdapter()
            adapter = coinInfoListAdapter
        }
        setUpOnClickListener()
    }
    private fun setUpOnClickListener(){
        coinInfoListAdapter.onCoinClickListener = {
            if (isOnePlaneMode()){
                val intent = CoinDetailActivity.newIntent(this, it.fromSymbol)
                startActivity(intent)
            }else{
                val fragment = CoinDetailFragment.newInstance(it.fromSymbol)
                launchFragmentMode(fragment)
            }
        }
    }
}
