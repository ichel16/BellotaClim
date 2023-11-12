package es.ljaraizc.bellotaclim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MaterialListAdapter extends ArrayAdapter<Material> {

    private List<Material> listaMateriales;
    private Context context;

    private int resource;

    public MaterialListAdapter(@NonNull Context context, int resource, List<Material> objects) {
        super(context, resource, objects);

        this.listaMateriales = objects;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null){

            view = LayoutInflater.from(context).inflate(resource, null);

        }

        Material material = listaMateriales.get(position);

        ImageView ivLVmaterialImagen = view.findViewById(R.id.ivLVmaterialImagen);
        ivLVmaterialImagen.setImageResource(material.getImagen());

        TextView tvLvmaterialTipo = view.findViewById(R.id.tvLvmaterialTipo);
        tvLvmaterialTipo.setText(material.getTipo());

        TextView tvLVmaterialMarca = view.findViewById(R.id.tvLVmaterialMarca);
        tvLVmaterialMarca.setText(material.getMarca());

        TextView tvLVmaterialModelo = view.findViewById(R.id.tvLVmaterialModelo);
        tvLVmaterialModelo.setText(material.getModelo());

        TextView tvLVmaterialTalla = view.findViewById(R.id.tvLVmaterialTalla);
        tvLVmaterialTalla.setText(material.getTalla());

        TextView tvLVmaterialNombre = view.findViewById(R.id.tvLVmaterialNombre);
        tvLVmaterialNombre.setText(material.getEmailEscalador());

        return view;
    }
}
