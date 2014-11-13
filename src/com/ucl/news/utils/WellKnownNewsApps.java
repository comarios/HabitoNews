package com.ucl.news.utils;

import java.util.ArrayList;
import java.util.List;

public class WellKnownNewsApps {

	private static WellKnownNewsApps instance = null;
	private List<String> newsApps = null;
	
	protected WellKnownNewsApps() {
		this.newsApps = new ArrayList<String>();
		init();
	}

	public static WellKnownNewsApps getInstance() {
		if (instance == null) {
			instance = new WellKnownNewsApps();
		}
		return instance;
	}
	
	private void init(){
		
		// Fill in ArrayList with well-known news apps
		newsApps.add("com.ucl.newsreader");
		newsApps.add("bbc.mobile.news.uk");
		newsApps.add("com.bskyb.skynews.android");
		newsApps.add("com.dailymail.online");
		newsApps.add("com.guardian");
		newsApps.add("com.reddit.iama");
		newsApps.add("com.buzzfeed.android");
		newsApps.add("com.google.android.apps.genie.geniewidget");
		newsApps.add("flipboard.app");
		newsApps.add("com.ideashower.readitlater.pro");
		newsApps.add("com.yahoo.mobile.client.android.yahoo");
		newsApps.add("com.yahoo.mobile.client.android.atom");
		newsApps.add("com.mirror.news");
		newsApps.add("uk.co.thesun.mobile");
		newsApps.add("com.devhd.feedly");
		newsApps.add("com.dailymail.tablet");
		newsApps.add("com.conduit.app_10d74b0898794e80bda42508f9203a57.app");
		newsApps.add("ie.slice.mylottouk");
		newsApps.add("com.itunestoppodcastplayer.app");
		newsApps.add("com.Samaatv.samaaapp3");
		newsApps.add("com.freerange360.mpp.indy");
		newsApps.add("com.cnn.mobile.android.phone");
		newsApps.add("com.ft.news");
		newsApps.add("com.huffingtonpost.android");
		newsApps.add("com.metro.metrotablet");
		newsApps.add("com.google.android.apps.currents");
		newsApps.add("com.google.android.apps.magazines");
		newsApps.add("com.aol.mobile.aolapp");
		newsApps.add("com.arynewslive.tv");
		newsApps.add("uk.co.economist");
		newsApps.add("com.onelouder.baconreader");
		newsApps.add("com.issuu.android.app");
		newsApps.add("com.mobilesrepublic.appy");
		newsApps.add("uk.co.telegraph.android");
		newsApps.add("net.aljazeera.english");
		newsApps.add("com.phyora.apps.reddit_now");
		newsApps.add("com.appliconic.pak.tv");
		newsApps.add("com.wiziapp.app104473");
		newsApps.add("com.ommdevil.android");
		newsApps.add("com.guardian.android.tabletedition.google");
		newsApps.add("net.mediaspectrum.dailymirror5androidreplica");
		newsApps.add("com.drippler.android.updates");
		newsApps.add("uk.co.thetimes");
		newsApps.add("com.zinio.mobile.android.reader");
		newsApps.add("com.wiziapp.app104331");
		newsApps.add("com.liverpool.echo");
		newsApps.add("com.rt.mobile.english");
		newsApps.add("com.androidcentral.app");
		newsApps.add("com.wiziapp.app104340");
		newsApps.add("com.itv.itvnewsapp");
		newsApps.add("com.mippin.android.bw.m1216");
		newsApps.add("ms.kslogix.arylivestreamhd");
		newsApps.add("com.manchester.evening.news");
		newsApps.add("com.barisefe.uknewspapers");
		newsApps.add("com.scottish.news");
		newsApps.add("mobi.beyondpod");
		newsApps.add("com.snr");
		newsApps.add("com.mobilesrepublic.appygeek");
		newsApps.add("com.thomsonreuters.reuters");
		newsApps.add("com.uk.newspaper");
		newsApps.add("ie.slice.mylottoireland");
		newsApps.add("com.freerange360.mpp.ThisIsLondon");
		newsApps.add("com.aol.mobile.engadget");
		newsApps.add("com.livenews");
		newsApps.add("tv.stv.android.news");
		newsApps.add("com.newsinternational.thesunclassic");
		newsApps.add("com.walesonline");
		newsApps.add("com.alphonso.pulse");
		newsApps.add("com.laurencedawson.reddit_sync");
		newsApps.add("net.jimblackler.newswidget");
		newsApps.add("com.mippin.android.bw.m1170");
		newsApps.add("com.deepnote.uknewspapers");
		newsApps.add("com.guides.minecraftnewmods.pe");
		newsApps.add("com.sfglobe.sfg");
		newsApps.add("com.redbull.redbulldotcom");
		newsApps.add("tv.u.android");
		newsApps.add("krant.newspapersUK");
		newsApps.add("com.eboundservices.express");
		newsApps.add("com.newcastle.chronicle");
		newsApps.add("com.appandmobile.bbcradiopodcasts");
		newsApps.add("com.toi.reader.activities");
		newsApps.add("uk.co.telegraph.kindlefire");
		newsApps.add("com.awnry.android.nawnry");
		newsApps.add("com.nytimes.android");
		newsApps.add("com.july.ndtv");
		newsApps.add("tv.dunyanews.ipad.v3");
		newsApps.add("com.fahmsoftnews");
		newsApps.add("com.guides.minecraftnewtp.pe");
		newsApps.add("tv.dunyanews.v2");
		newsApps.add("com.androidauthority");
		newsApps.add("uk.co.ni.times");
		newsApps.add("com.mippin.android.bw.m14581");
		newsApps.add("samsungupdate.com");
		newsApps.add("com.foxnews.android");
		newsApps.add("com.bryant.newsnow");
		newsApps.add("com.sothree.umano");
		newsApps.add("com.vice.viceforandroid");
		newsApps.add("com.pagesuite.droid.thesundaysport");
		newsApps.add("pl.tvn.android.tvn24");
		newsApps.add("com.briox.riversip.android.premier.transfers");
		newsApps.add("bbc.news.mobile.cymru");
		newsApps.add("com.treemolabs.apps.cnet");
		newsApps.add("sigmalive.news.com");
		newsApps.add("com.appbaker.app1581159");
		newsApps.add("com.cyprus.newspapers");
		newsApps.add("com.aspectsense.cyprusnews");
		newsApps.add("com.troktiko");
		newsApps.add("com.inbusiness.news");
		newsApps.add("com.mike.cytechnews");
		newsApps.add("inb.imh.com.cy");
		newsApps.add("com.philenews");
		newsApps.add("gr.skai.skai_android");
		newsApps.add("gr.truthmedia.alithiagr");
		newsApps.add("com.conduit.app_a853843d7aa04a04a63d0314fb3a0ee6.app");
	}
	
	public Boolean isNewsAppExist(String newsApp) {
		for (String c : newsApps) {
			if (c.equals(newsApp)) 
				return true;
		}
		return false;
	}
}
