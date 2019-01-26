package com.code200dev.agenda

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu

import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_lista_contatos.*
import android.widget.ArrayAdapter
import android.widget.Toast
import com.code200dev.agenda.model.Contato
import com.code200dev.agenda.repository.ContatoRepository
import kotlinx.android.synthetic.main.content_lista_contatos.*

class ListaContatosActivity : AppCompatActivity() {

    private var contatos:ArrayList<Contato>? = null
    private var contatoSelecionado:Contato? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_contatos);

        val myToolbar = minhaToolbar
        myToolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(myToolbar)

        contatos = ContatoRepository(this).findAll()
        val adapter= ArrayAdapter(this, android.R.layout.simple_list_item_1, contatos)
        lista?.adapter = adapter
        adapter.notifyDataSetChanged()

        lista.setOnItemClickListener { _, _, position, id ->
            val intent = Intent(this@ListaContatosActivity, ContatoActivity::class.java)
            intent.putExtra("contato", contatos?.get(position))
            startActivity(intent)
        }

        lista.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapter, view, posicao, id ->
            contatoSelecionado = contatos?.get(posicao)
            false
        }



    }

    override fun onResume() {
        super.onResume()
        contatos = ContatoRepository(this).findAll()
        val adapter= ArrayAdapter(this, android.R.layout.simple_list_item_1, contatos)
        lista?.adapter = adapter
        adapter.notifyDataSetChanged()

        carregaLista()
        registerForContextMenu(lista);

    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        menuInflater.inflate(R.menu.menu_contato_contexto, menu)
        super.onCreateContextMenu(menu, v, menuInfo)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.excluir -> {
                AlertDialog.Builder(this@ListaContatosActivity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar ?")
                        .setPositiveButton("Quero",
                                )
                                DialogInterface.OnClickListener { dialog, which ->
                                    ContatoRepository(this).delete(this.contatoSelecionado!!.id.toLong())
                                    carregaLista()
                                }).setNegativeButton("Nao", null).show()
                return false
            }
            else -> return super.onContextItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.novo -> {

                val intent = Intent(this, ContatoActivity::class.java)
                startActivity(intent)
                return false

            }

            R.id.sincronizar -> {
                Toast.makeText(this, "Enviar", Toast.LENGTH_LONG).show()
                return false
            }

            R.id.receber -> {
                Toast.makeText(this, "Receber", Toast.LENGTH_LONG).show()
                return false
            }

            R.id.mapa -> {
                Toast.makeText(this, "Mapa", Toast.LENGTH_LONG).show()
                return false
            }

            R.id.enviasms -> {
                val intentSms = Intent(Intent.ACTION_VIEW)
                intentSms.data = Uri.parse("sms:" + contatoSelecionado?.telefone)
                intentSms.putExtra("sms_body", "Mensagem")
                item.intent = intentSms
                return false
            }

            R.id.enviaemail -> {
                val intentEmail = Intent(Intent.ACTION_SEND)
                intentEmail.type = "message/rfc822"
                intentEmail.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(contatoSelecionado?.email!!))
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Teste de email")
                intentEmail.putExtra(Intent.EXTRA_TEXT, "Corpo da mensagem")
                //item.setIntent(intentEmail);
                startActivity(Intent.createChooser(intentEmail, "Selecione a sua aplicação de Email"))
                return false
            }

            R.id.share -> {
                val intentShare = Intent(Intent.ACTION_SEND)
                intentShare.type = "text/plain"
                intentShare.putExtra(Intent.EXTRA_SUBJECT, "Assunto que será compartilhado")
                intentShare.putExtra(Intent.EXTRA_TEXT, "Texto que será compartilhado")
                startActivity(Intent.createChooser(intentShare, "Escolha como compartilhar"))
                return false
            }

            R.id.ligar -> {
                val intentLigar = Intent(Intent.ACTION_DIAL)
                intentLigar.data = Uri.parse("tel:" + contatoSelecionado?.telefone)
                item.intent = intentLigar
                return false
            }

            R.id.visualizarmapa -> {
                val gmmIntentUri = Uri.parse("geo:0,0?q=" + contatoSelecionado?.endereco)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
                return false
            }


            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun carregaLista() {
        contatos = ContatoRepository(this).findAll()
        val adapter= ArrayAdapter(this, android.R.layout.simple_list_item_1, contatos)
        lista?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}

