package com.example.navigationadtest.ui.home

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.navigationadtest.databinding.FragmentHomeBinding
import com.example.navigationadtest.ui.account.TAG
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private var adView: AdView? = null

    // Determine the screen width (less decorations) to use for the ad width.
    // If the ad hasn't been laid out, default to the full screen width.
    private val adSize: AdSize
        get() {
            val display = requireActivity().windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding.bannerAdContainer.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(requireContext(), adWidth)
        }

    private fun loadBanner() {
        adView = AdView(requireContext())
        adView!!.adUnitId = "ca-app-pub-3940256099942544/9214589741"

        adView!!.setAdSize(adSize)

        // Create an extra parameter that aligns the bottom of the expanded ad to
        // the bottom of the bannerView.
        val extras = Bundle()
        extras.putString("collapsible", "bottom")

        val adRequest = AdRequest.Builder()
//            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()

        adView!!.loadAd(adRequest)

        adView!!.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                binding.bannerAdContainer.removeAllViews()
                binding.bannerAdContainer.addView(adView)
            }
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                adView?.pause()
                Log.d(TAG, "HomeFragment onPause: adView?.pause()")
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                adView?.resume()
                Log.d(TAG, "HomeFragment onResume: adView?.resume()")
            }

            override fun onDestroy(owner: LifecycleOwner) {
                adView?.destroy()
                Log.d(TAG, "HomeFragment onDestroy: adView?.destroy()")
                super.onDestroy(owner)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnNext.setOnClickListener {

            val action = HomeFragmentDirections
                .actionNavHomeToAccountFragment()
            findNavController().navigate(action)

        }

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBanner()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}