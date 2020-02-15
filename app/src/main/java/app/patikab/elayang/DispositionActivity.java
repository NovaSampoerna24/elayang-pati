package app.patikab.elayang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.patikab.elayang.adapter.ListContactPenerimaAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import app.patikab.elayang.adapter.ListContactAdapter;
import app.patikab.elayang.adapter.ListInstructionAdapter;
import app.patikab.elayang.jsonReq.AddDisposisi;
import app.patikab.elayang.model.Instruction;
import app.patikab.elayang.model.User;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Endpoint;
import app.patikab.elayang.util.RequestHandler;

public class DispositionActivity extends AppCompatActivity
        implements ListInstructionAdapter.IInstructionAdapter, RequestHandler.IParse, ListContactAdapter.IUserAdapter, ListContactPenerimaAdapter.IUserAdapter {

    @BindView(R.id.linearPenerima)
    LinearLayout lnPenerima;
    @BindView(R.id.linearInstruksi)
    LinearLayout lnInstruksi;
    @BindView(R.id.linearIsi)
    LinearLayout lnIsi;

    @BindView(R.id.linearlistIntruksi)
    LinearLayout linearlistIntruksi;

    @BindView(R.id.textViewTanggal)
    TextView tvTanggal;
    @BindView(R.id.textViewPerihal)
    TextView tvPerihal;

    RecyclerView rvInstruksi;
    ListInstructionAdapter instructionAdapter;

    List<Instruction> instructions = new ArrayList<>();


    Boolean isFiltered = false;


    RecyclerView rvContact,rvlistpenerima;
    ListContactAdapter contactAdapter;
    ListContactPenerimaAdapter listContactPenerimaAdapter;
    List<User> users = new ArrayList<>();
    List<User> filteredItems = new ArrayList<>();
    List<AddDisposisi> selectedPenerima = new ArrayList<>();

    EditText etIsi;

    BottomSheetDialog dialogContact;
    BottomSheetDialog dialogInstruction;
    BottomSheetDialog dialogMessage;

    String idSurat;
    String nipPenerima;
    String namaPenerima;
    String jabatanPenerima;
    String instruksi;
    String intruksinama;
    String intruksidetail;
    String isiDisposisi = "-";
    String dataSurat;

    TextView txt_intruksi,txt_namaintruksi;

    @BindView(R.id.buttonDisposisi)
    Button btnDisposisi;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    int totalDispo = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disposition);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        idSurat = getIntent().getStringExtra(Config.INTENT_ID_MAIL);
        dataSurat = getIntent().getStringExtra(Config.INTENT_DATA);

        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(Config.INTENT_DATA));
            tvPerihal.setText(jsonObject.getString("perihal"));
            tvTanggal.setText(Config.defTanggal(jsonObject.getString("tgl_terima")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialogContact = new BottomSheetDialog(this);
        dialogInstruction = new BottomSheetDialog(this);
        dialogMessage = new BottomSheetDialog(this);

        instructionAdapter = new ListInstructionAdapter(this, this, instructions);
        contactAdapter = new ListContactAdapter(this, this, users);
        listContactPenerimaAdapter = new ListContactPenerimaAdapter(this,this,selectedPenerima);
        initBottomDialog();

        lnPenerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogContact.show();
            }
        });

        lnInstruksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInstruction.show();


            }
        });

        lnIsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMessage.show();
            }
        });

        btnDisposisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqAddDisposition();
            }
        });

        reqMasterInstruksi();
        reqListContact();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("type-instruksi"));
    }

    private void reqMasterInstruksi() {
        RequestHandler handler = new RequestHandler(this, this);
        handler.request(Request.Method.GET, Endpoint.MASTER_INSTRUKSI, null);
    }

    private void reqListContact() {
        RequestHandler handler = new RequestHandler(this, this);
        handler.request(Request.Method.GET, Endpoint.LIST_USER_DISPOSITION + idSurat, null);
    }

    private void reqAddDisposition() {

        if (validasi()) {
            totalDispo = selectedPenerima.size();
            for (int i = 0; i < selectedPenerima.size(); i++) {

                AddDisposisi ad = selectedPenerima.get(i);

                AddDisposisi addDisposisi = new AddDisposisi();
                addDisposisi.setId_surat_masuk(idSurat);
                addDisposisi.setNip_penerima(ad.getNip_penerima());
                addDisposisi.setNama_penerima(ad.getNama_penerima());
                addDisposisi.setJabatan_penerima(ad.getJabatan_penerima());
                addDisposisi.setInstruksi(instruksi);
                addDisposisi.setIsi_disposisi(isiDisposisi);
//                addDisposisi.setIsi_disposisi(isiDisposisi);

                String reqJson = new Gson().toJson(addDisposisi);

                try {
                    RequestHandler handler = new RequestHandler(this, this);
                    handler.request(Request.Method.POST, Endpoint.DISPOSISI_ADD, new JSONObject(reqJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            btnDisposisi.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private boolean validasi() {
        Log.i("val", nipPenerima + namaPenerima + jabatanPenerima);
        if (selectedPenerima.size() < 1) {
            Snackbar.make(tvPerihal, "Silakan pilih tujuan disposisi terlebih dahulu", Snackbar.LENGTH_LONG).show();
        } else {
            if (instruksi == null) {
                Snackbar.make(tvPerihal, "Silakan tentukan instruksi terlebih dahulu", Snackbar.LENGTH_LONG).show();
            } else {
                return true;
                /*if (isiDisposisi == null) {
                    Snackbar.make(tvPerihal, "Silakan isi disposisi terlebih dahulu", Snackbar.LENGTH_LONG).show();
                } else {

                }*/
            }
        }
        return false;
    }


    private void initBottomDialog() {
        View sheetView = null;

//        if (i == 1)
        sheetView = getLayoutInflater().inflate(R.layout.content_contact, null);
        rvContact = sheetView.findViewById(R.id.recyclerViewContact);
        rvContact.setLayoutManager(new GridLayoutManager(this, 3));
        rvContact.setAdapter(contactAdapter);

        rvlistpenerima = findViewById(R.id.listpenerima);
        rvlistpenerima.setLayoutManager(new LinearLayoutManager(this));
        rvlistpenerima.setAdapter(listContactPenerimaAdapter);


        final EditText editText = sheetView.findViewById(R.id.editTextQuery);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchItem(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sheetView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogContact.dismiss();
            }
        });
        dialogContact.setContentView(sheetView);

//        else if (i == 2) {
        sheetView = getLayoutInflater().inflate(R.layout.content_instruction, null);
        rvInstruksi = sheetView.findViewById(R.id.recyclerViewInstruksi);
        rvInstruksi.setLayoutManager(new LinearLayoutManager(this));
        rvInstruksi.setAdapter(instructionAdapter);

        sheetView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInstruction.dismiss();
            }
        });
        dialogInstruction.setContentView(sheetView);


//        } else {
        sheetView = getLayoutInflater().inflate(R.layout.content_message, null);
        etIsi = sheetView.findViewById(R.id.editTextIsi);
        Button btnSimpan = sheetView.findViewById(R.id.buttonSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveMessage();
            }
        });
        sheetView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMessage.dismiss();
            }
        });
        dialogMessage.setContentView(sheetView);
//        }


    }

    private void onSaveMessage() {
        ImageView ivIsi = findViewById(R.id.imageViewIsi);
        ivIsi.setImageResource(R.drawable.ic_edit_blue_24dp);
        TextView tvIsi = findViewById(R.id.textViewIsi);
        tvIsi.setTextColor(getResources().getColor(R.color.colorAccent));

        isiDisposisi = etIsi.getText().toString();
        txt_namaintruksi.setText(isiDisposisi);
//        Log.d("post2",isiDisposisi);
        reqAddDisposition();
        dialogInstruction.dismiss();
        dialogMessage.dismiss();
    }


    @Override
    public void onClickInstruksi(String id, String data, View view) {
        ImageView ivInstruksi = findViewById(R.id.imageViewInstruksi);
        ivInstruksi.setImageResource(R.drawable.ic_assignment_blue_24dp);
        TextView tvInstruksi = findViewById(R.id.textViewInstruksi);
        tvInstruksi.setTextColor(getResources().getColor(R.color.colorAccent));

//        dialogInstruction.dismiss();

        instruksi = id;
        linearlistIntruksi.setVisibility(View.VISIBLE);
         txt_intruksi = findViewById(R.id.txt_intruksi);
         txt_namaintruksi = findViewById(R.id.txt_detailintruksi);
        try {
            JSONObject object = new JSONObject(data);
            intruksinama = object.getString("nama");
            txt_intruksi.setText(intruksinama);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("dataintruksi",data);
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dialogMessage.show();
        }
    };

    @Override
    public void onClickContact(String id, String data, View view) {
        ImageView ivPenerima = findViewById(R.id.imageViewPenerima);
        TextView tvPenerima = findViewById(R.id.textViewPenerima);


        try {
            Log.i("jabatan", data);
            JSONObject object = new JSONObject(data);
            nipPenerima = object.getString("nip");
            namaPenerima = object.getString("nama_lengkap");
            jabatanPenerima = object.getString("nama_jabatan");

            boolean isSame = false;
            int idx = 0;
            for (int i = 0; i < selectedPenerima.size(); i++) {
                if (selectedPenerima.get(i).getNip_penerima().equals(nipPenerima)) {
                    isSame = true;
                    idx = i;
                    break;
                }
            }
            if (isSame) {
                selectedPenerima.remove(idx);
            } else {
                AddDisposisi ad = new AddDisposisi();
                ad.setNip_penerima(nipPenerima);
                ad.setNama_penerima(namaPenerima);
                ad.setJabatan_penerima(jabatanPenerima);
                selectedPenerima.add(ad);
                listContactPenerimaAdapter.notifyDataSetChanged();
            }

            if (selectedPenerima.size() > 0) {
                ivPenerima.setImageResource(R.drawable.ic_person_outline_blue_24dp);
                tvPenerima.setTextColor(getResources().getColor(R.color.colorAccent));
            } else {
                ivPenerima.setImageResource(R.drawable.ic_person_outline_black_24dp);
                tvPenerima.setTextColor(getResources().getColor(R.color.colorBlack));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseResponses(Boolean isSuccess, String method, String data) {
        btnDisposisi.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        if (isSuccess) {
            if (method.equals(Endpoint.MASTER_INSTRUKSI)) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Instruction instruction = new Instruction();
                        instruction.setId(o.getString("id"));
                        instruction.setNama(o.getString("nama"));
                        instructions.add(instruction);
                    }
                    instructionAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.contains(Endpoint.LIST_USER_DISPOSITION)) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        User user = new User();
                        user.setNip(o.getString("nip"));
                        user.setNama_lengkap(o.getString("nama_lengkap"));
                        user.setNama_jabatan(o.getString("nama_jabatan"));
                        user.setFoto(o.getString("foto"));
                        users.add(user);
                    }
                    contactAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method.equals(Endpoint.DISPOSISI_ADD)) {
                totalDispo -= 1;
                if (totalDispo == 0) {
                    try {
                        JSONObject object = new JSONObject(data);
                        Toast.makeText(this, "Berhasil membuat disposisi", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void searchItem(String query) {
        if (!isFiltered) {
            filteredItems.clear();
            filteredItems.addAll(users);
            isFiltered = true;
        }
        users.clear();
        if (query == null || query.isEmpty()) {
            users.addAll(filteredItems);
            isFiltered = false;
        } else {
            for (User user : filteredItems) {
                if (user.getNama_lengkap().toLowerCase().contains(query)) {
                    users.add(user);
                }
            }
        }
        contactAdapter.notifyDataSetChanged();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
