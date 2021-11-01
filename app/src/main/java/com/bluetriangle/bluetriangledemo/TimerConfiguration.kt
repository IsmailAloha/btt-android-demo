package com.bluetriangle.bluetriangledemo

import com.bluetriangle.analytics.Timer

data class TimerConfiguration(
    var abTestId: String = "",
    var brandValue: Double? = null,
    var campaignMedium: String = "",
    var campaignName: String = "",
    var campaignSource: String = "",
    var cartValue: Double? = null,
    var contentGroupName: String = "",
    var orderNumber: String = "",
    var orderTime: Long? = null,
    var pageName: String = "",
    var pageValue: Double? = null,
    var referrer: String = "",
    var timeOnPage: Long? = null,
    var trafficSegmentName: String = "",
    var url: String = ""
)

fun Timer.configure(config: TimerConfiguration) {
    setPageName(config.pageName)
    setTrafficSegmentName(config.trafficSegmentName)
    setAbTestIdentifier(config.abTestId)
    setContentGroupName(config.contentGroupName)
    config.brandValue?.let {
        setBrandValue(it)
    }
    config.cartValue?.let {
        setCartValue(it)
    }
    setOrderNumber(config.orderNumber)
    config.orderTime?.let {
        setOrderTime(it)
    }
    config.pageValue?.let {
        setPageValue(it)
    }
    setCampaignName(config.campaignName)
    setCampaignSource(config.campaignSource)
    setCampaignMedium(config.campaignMedium)
    config.timeOnPage?.let {
        setTimeOnPage(it)
    }
    setUrl(config.url)
    setReferrer(config.referrer)
}