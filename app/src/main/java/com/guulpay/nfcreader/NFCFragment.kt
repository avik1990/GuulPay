package com.guulpay.nfcreader

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Constants
import com.guulpay.others.NFCUtils
import com.guulpay.others.NdefMessageParser
import com.guulpay.payments.PaymentActivity
import kotlinx.android.synthetic.main.scan_nfc_fragment.*


class NFCFragment : BaseFragment(), View.OnClickListener, NFCContract.View {
    override fun globalLogout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var scanQrPresenter: NFCPresenter
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var mContext: Context? = null

    companion object {
        const val CLASS_NAME = "NFCFragment"
        fun newInstance(fromWhichFragment: String): Fragment {
            val fragment = NFCFragment()
            val bundle = Bundle()
            bundle.putString(Constants.Keys.KEY_FRAGMENT_NAME, fromWhichFragment)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(): Fragment {
            val fragment = NFCFragment()
            // val bundle = Bundle()
            //bundle.putString(Constants.Keys.KEY_FRAGMENT_NAME, fromWhichFragment)
            // fragment.arguments = bundle
            return fragment
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun getLayoutView(): Int {
        return R.layout.scan_nfc_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        NFCPresenter(this).start()
        mContext = activity
        nfcAdapter = NfcAdapter.getDefaultAdapter(mContext)

    }

    override fun initListeners() {
        if (nfcAdapter == null) {
            Toast.makeText(activity, "No NFC", Toast.LENGTH_SHORT).show()
            Log.d("NotWorking", "NotWorking")
            return
        }
        pendingIntent = PendingIntent.getActivity(this.activity, 0, Intent(mContext, this.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
    }

    override fun onClick(v: View?) {
        /* if (v == imgvwFlashOnOff) {
             scanQrPresenter.handleCameraFlash()
         } else if (v == imgvwOpenGallery) {
             openGallery()
         }*/
    }

    private fun openGallery() {

    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }*/


    /* NFCContract.View callback methods */

    override fun handleProgressAlert(showingStatus: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToNextPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as PaymentActivity).isActivityVisible
    }

    override fun setPresenter(presenter: NFCContract.Presenter) {
        scanQrPresenter = presenter as NFCPresenter
    }

    override fun isNetworkAvailable(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNetworkUnavailableMsg() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContext(): Context {
        return context
    }

    override fun getArgument(): Bundle? {
        return arguments
    }

    override fun onPause() {
        super.onPause()
    }

    private fun showNFCSettings() {
        Toast.makeText(context, "You need to enable NFC", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        val nfcAdapterRefCopy = nfcAdapter
        if (nfcAdapterRefCopy != null) {
            if (!nfcAdapterRefCopy.isEnabled())
                showNFCSettings()
            nfcAdapterRefCopy.enableForegroundDispatch(activity, pendingIntent, null, null)
        }
    }

    private fun dumpTagData(tag: Tag): String {
        val sb = StringBuilder()
        val id = tag.getId()
        sb.append("ID (hex): ").append(NFCUtils.toHex(id)).append('\n')
        sb.append("ID (reversed hex): ").append(NFCUtils.toReversedHex(id)).append('\n')
        sb.append("ID (dec): ").append(NFCUtils.toDec(id)).append('\n')
        sb.append("ID (reversed dec): ").append(NFCUtils.toReversedDec(id)).append('\n')

        val prefix = "android.nfc.tech."
        sb.append("Technologies: ")
        for (tech in tag.getTechList()) {
            sb.append(tech.substring(prefix.length))
            sb.append(", ")
        }

        sb.delete(sb.length - 2, sb.length)

        for (tech in tag.getTechList()) {
            if (tech == MifareClassic::class.java.name) {
                sb.append('\n')
                var type = "Unknown"

                try {
                    val mifareTag = MifareClassic.get(tag)

                    when (mifareTag.type) {
                        MifareClassic.TYPE_CLASSIC -> type = "Classic"
                        MifareClassic.TYPE_PLUS -> type = "Plus"
                        MifareClassic.TYPE_PRO -> type = "Pro"
                    }
                    sb.append("Mifare Classic type: ")
                    sb.append(type)
                    sb.append('\n')

                    sb.append("Mifare size: ")
                    sb.append(mifareTag.size.toString() + " bytes")
                    sb.append('\n')

                    sb.append("Mifare sectors: ")
                    sb.append(mifareTag.sectorCount)
                    sb.append('\n')

                    sb.append("Mifare blocks: ")
                    sb.append(mifareTag.blockCount)
                } catch (e: Exception) {
                    sb.append("Mifare classic error: " + e.message)
                }

            }

            if (tech == MifareUltralight::class.java.name) {
                sb.append('\n')
                val mifareUlTag = MifareUltralight.get(tag)
                var type = "Unknown"
                when (mifareUlTag.type) {
                    MifareUltralight.TYPE_ULTRALIGHT -> type = "Ultralight"
                    MifareUltralight.TYPE_ULTRALIGHT_C -> type = "Ultralight C"
                }
                sb.append("Mifare Ultralight type: ")
                sb.append(type)
            }
        }

        return sb.toString()
    }


    fun resolveIntent(intent: Intent) {
        val action = intent.action

        if (NfcAdapter.ACTION_TAG_DISCOVERED == action
                || NfcAdapter.ACTION_TECH_DISCOVERED == action
                || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            if (rawMsgs != null) {
                Log.i("NFC", "Size:" + rawMsgs.size);
                val ndefMessages: Array<NdefMessage> = Array(rawMsgs.size, { i -> rawMsgs[i] as NdefMessage });
                displayNfcMessages(ndefMessages)
            } else {
                val empty = ByteArray(0)
                val id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
                val tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag
                val payload = dumpTagData(tag).toByteArray()
                val record = NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload)
                val emptyMsg = NdefMessage(arrayOf(record))
                val emptyNdefMessages: Array<NdefMessage> = arrayOf(emptyMsg);
                displayNfcMessages(emptyNdefMessages)
            }
        }
    }


    private fun displayNfcMessages(msgs: Array<NdefMessage>?) {
        if (msgs == null || msgs.isEmpty())
            return

        val builder = StringBuilder()
        val records = NdefMessageParser.parse(msgs[0])
        val size = records.size

        for (i in 0 until size) {
            val record = records[i]
            val str = record.str()
            builder.append(str).append("\n")
        }

        qrTextTag?.setText(builder.toString())
    }

}