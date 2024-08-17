package dev.answer.test;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import dev.answer.pinetool.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Inflate and get instance of binding
    binding = ActivityMainBinding.inflate(getLayoutInflater());

    // set content view to binding's root
    setContentView(binding.getRoot());

    binding.hook.setOnClickListener(v -> Main.main());
    binding.run.setOnClickListener(v -> {
          Main.method_1();
          Main.method_2();
        });
  }
}
