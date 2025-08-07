package pagkage here;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sobeeKinun.addminapp.R;
import com.sobeeKinun.addminapp.databinding.ActivityCouponBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CouponActivity extends AppCompatActivity {

    ActivityCouponBinding binding;

    List<ProductModel> productList = new ArrayList<>();
    ProductAdapter adapter = new ProductAdapter(productList);
    ExecutorService executor = Executors.newSingleThreadExecutor(); // Background thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCouponBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Status bar settings
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.light_gray));
        }

        // Edge padding for safe insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // RecyclerView setup
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // Product click
        adapter.setOnItemClickListener(product -> {
            Toast.makeText(this, "Selected: " + product.getId(), Toast.LENGTH_SHORT).show();
            // You can auto-fill coupon field here if needed
            // binding.editTextCouponId.setText(product.getId());
        });

        // Search listener
        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    executor.execute(() -> searchProducts(query)); // Run in background
                } else {
                    productList.clear();
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Back button
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void searchProducts(String query) {

        try {
            URL url = new URL("https://yourDomain.com/Prodcut/search_products.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("query", query);

            OutputStream os = conn.getOutputStream();
            os.write(jsonParam.toString().getBytes("UTF-8"));
            os.close();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            JSONArray array = new JSONArray(response.toString());
            productList.clear();


            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                ProductModel product = new ProductModel(
                        obj.getString("product_id"),
                        obj.getString("product_name")
                );
                productList.add(product);

            }

            runOnUiThread(() -> adapter.notifyDataSetChanged());

            reader.close();
            conn.disconnect();
        } catch (Exception e) {

            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(this, "Error loading products", Toast.LENGTH_SHORT).show());
        }
    }
}
