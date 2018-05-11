package edu.mga.knight_rider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.mga.knight_rider.models.User;
import edu.mga.knight_rider.network.UserDataService;
import edu.mga.knight_rider.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    protected DrawerLayout sidebar;
    private SharedPreferences prefs;

    protected void onCreateDrawer()
    {
        prefs = this.getSharedPreferences("edu.mga.knightrider", Context.MODE_PRIVATE);
        // R.id.drawer_layout should be in every activity with exactly the same id.
        sidebar = findViewById(R.id.drawer_layout);

        // Checks to see if token exists before attempting to access data => IMPORTANT (Since initially, main activity loads, then redirects to login)
        if (!prefs.getString("knight-rider-token", "").isEmpty()) {
            UserDataService service = RetrofitInstance.getRetrofitInstance().create(UserDataService.class); // Instantiate our service
            Call<User> call = service.getUser("Bearer " + prefs.getString("knight-rider-token",null), prefs.getString("knight-rider-userid", null)); // Fill our request template

            // Make request and setup callback to handle response
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User currentUser = response.body();
                        showUser(currentUser);
                    } else {
                        Toast.makeText(BaseActivity.this, "Network request failed to obtain user data.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(BaseActivity.this, "Unable to load profile information.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MENU", Integer.toString(item.getItemId()));
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_home:
                intent = new Intent(BaseActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_find_rides:
                intent = new Intent(BaseActivity.this, FindRideActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_logout:
                intent = new Intent(BaseActivity.this, LoginActivity.class);
                prefs.edit().clear().commit();
                startActivity(intent);
                finish();
                return true;
            default:
                Toast.makeText(this, "Ooops, not implemented yet.", Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    private void showUser(User currentUser) {
        CircleImageView profilePic = (CircleImageView) findViewById(R.id.profile_pic);
        TextView profileName = (TextView) findViewById(R.id.profile_name);
        TextView profileEmail = (TextView) findViewById(R.id.profile_email);

        Glide.with(this)
                .load(currentUser.getProfilePicture() == null ? "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxBlv5RzA3udThbSqdYWNkVkL2GDMWdwHB47qKzaTfOhYm-943" : currentUser.getProfilePicture())
                .into(profilePic);

        profileName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        profileEmail.setText(currentUser.getUsername());
    }
}
