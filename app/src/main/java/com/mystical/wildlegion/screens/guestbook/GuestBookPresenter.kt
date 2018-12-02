package com.mystical.wildlegion.screens.guestbook

import com.mystical.wildlegion.screens.guestbook.models.Entry
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist

class GuestBookPresenter(var mView: GuestBookContract.View) : GuestBookContract.Presenter {

    var mEntries = mutableListOf<Entry>()

    companion object {
        const val GUEST_BOOK_URL = "http://users4.smartgb.com/g/g.php?a=s&i=g44-71854-e8&m=all&p=1"
    }

    override fun start() {
        mView.initializeActionBar()
        mView.setActionbarTitle("Guest book")
        mView.setupRecyclerView()
        loadEntries()
    }

    fun loadEntries() {
        Observable.fromCallable { Jsoup.connect(GUEST_BOOK_URL).maxBodySize(0).get() }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                        onError = mView::onLoadFailed,
                        onNext = {
                            val headerParagraph = it.getElementsByTag("p")[1]
                            mView.setHeaderStats(headerParagraph.child(1).text())

                            mEntries = it.select("table[width=67%]")
                                    .map {
                                        val entry = Entry()
                                        val headerTable = it.select("table[width=100%] table")[0]
                                        headerTable.select("tr")
                                                .forEachIndexed { index, element ->
                                                    when (index) {
                                                        0 -> entry.date = element.select("td")[1].text()
                                                        1 -> entry.name = element.select("td")[1].text()
                                                        2 -> entry.number = element.select("td")[1].text().toInt()
                                                    }
                                                }

                                        val content = it.select("div.divmess")
                                        entry.body = Jsoup.clean(content.html(), "",
                                                Whitelist.none(), Document.OutputSettings().prettyPrint(false)).trim()
                                        entry
                                    }
                                    .toMutableList()

                            mView.showEntries(mEntries)
                        }
                )
    }
}