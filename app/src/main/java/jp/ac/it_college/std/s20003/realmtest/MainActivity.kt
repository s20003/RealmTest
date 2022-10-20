package jp.ac.it_college.std.s20003.realmtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = ListAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.memo_list)
        recyclerView.adapter = adapter

        val editText = findViewById<EditText>(R.id.memo_edit_text)
        val addButton = findViewById<Button>(R.id.add_button)

        val realm = Realm.getDefaultInstance()

        addButton.setOnClickListener {
            val text = editText.text.toString()
            if (text.isEmpty()) {
                return@setOnClickListener
            }
            realm.executeTransactionAsync {
                val memo = it.createObject(Memo::class.java)
                memo.name = text
                it.copyFromRealm(memo)
            }
            editText.text.clear()
        }
        realm.addChangeListener {
            // 変更があった時にリストをアップデートする
            val memoList = it.where(Memo::class.java).findAll().map { it.name }
            // UIスレッドで更新する
            recyclerView.post {
                adapter.updateMemoList(memoList)
            }
        }
        // 初回表示時にリストを表示
        realm.executeTransactionAsync {
            val memoList = it.where(Memo::class.java).findAll().map { it.name }
            // UIスレッドで更新する
            recyclerView.post {
                adapter.updateMemoList(memoList)
            }
        }
    }
}