package com.example.fajar.verifikasiimb.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fajar.verifikasiimb.DetailBangunanActivity;
import com.example.fajar.verifikasiimb.R;
import com.example.fajar.verifikasiimb.adapter.DividerItemDecoration;
import com.example.fajar.verifikasiimb.adapter.MyItemRecyclerViewAdapter;
import com.example.fajar.verifikasiimb.listener.RecyclerItemClickListener;
import com.example.fajar.verifikasiimb.model.Bangunan;
import com.example.fajar.verifikasiimb.model.BangunanResponse;
import com.example.fajar.verifikasiimb.rest.ApiClient;
import com.example.fajar.verifikasiimb.rest.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    private MapsFragment mapsFragment = new MapsFragment();

    public ProgressBar progressBar;


    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        ApiService apiService = ApiClient.NewClient().create(ApiService.class);

        Call<BangunanResponse> call = apiService.Bangunanku();
        call.enqueue(new Callback<BangunanResponse>() {

            @Override
            public void onResponse(Call<BangunanResponse> call, retrofit2.Response<BangunanResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Bangunan> bangunan = response.body().getBangunan(getActivity().getApplicationContext());
                    mapsFragment.setListBangunan(bangunan);
                    //Log.i("tugascontent", response.body().getBangunan().toString());
                    Log.i("tugascontent", response.body().toString());
                    Log.i("CodeTugas", String.valueOf(statusCode));
//                Toast.makeText(getContext(),""+response, Toast.LENGTH_SHORT).show();
                    setBangunanAdapter(getView(), bangunan);
                } else {
                    Toast.makeText(getContext(), "" + response+"failed to get response", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BangunanResponse> call, Throwable t) {
                Log.e(String.valueOf(getActivity().getApplicationContext()), t.toString());
                Log.i("retrofit", t.getMessage());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

//        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        return view;
    }

    protected void setBangunanAdapter(View view, final List<Bangunan> bangunan) {
        // Set the adapter
        if (view instanceof RecyclerView && bangunan != null) {
            final Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(bangunan, mListener, context));
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //do whatever
                            Bangunan bg = bangunan.get(position);
                            Intent i = new Intent(context, DetailBangunanActivity.class);
                            Bundle extras = new Bundle();
                            extras.putInt("id", bg.getId());
                            extras.putInt("position", position);
                            extras.putInt("fid", bg.getFid());
                            extras.putInt("nib", bg.getNib());
                            extras.putInt("sk", bg.getNoSk());
                            extras.putInt("imb", bg.getIdKetImb());
                            extras.putString("ket_imb", bg.getKetImb());
                            extras.putInt("persil", bg.getNoPersil());
                            extras.putString("pemilik", bg.getNama());
                            extras.putString("wilayah", bg.getNamaWilayah());
                            extras.putString("landuse", bg.getTipeLanduse());
                            extras.putString("kelurahan", bg.getKelurahan());
                            extras.putString("kecamatan", bg.getNamaKecamatan());
                            extras.putString("namasite", bg.getNamaSite());
                            extras.putString("namajalan", bg.getNamaJalan());
                            extras.putString("gang", bg.getGang());
                            extras.putString("nomor", bg.getNomor());
                            extras.putDouble("latitude", bg.getLatitude());
                            extras.putDouble("longitude", bg.getLongitude());
                            extras.putString("encoded_image", bg.getGambarBangunan());

                            i.putExtras(extras);
                            startActivity(i);
//                            getActivity().finish();
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            Toast.makeText(context, "Item Telah Di Pencet", Toast.LENGTH_SHORT).show();
                        }
                    })
            );

        } else {
            Toast.makeText(getActivity(), "the data is null", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Bangunan item);
    }


}
