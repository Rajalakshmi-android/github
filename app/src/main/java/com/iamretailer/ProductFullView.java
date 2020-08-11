package com.iamretailer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.ColorAdapters;
import com.iamretailer.Adapter.CommonAdapter;
import com.iamretailer.Adapter.ReviewAdapter;
import com.iamretailer.Adapter.SizeOptionAdapters;
import com.iamretailer.Adapter.SliderAdapter;
import com.iamretailer.Adapter.SpinnerAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.Helper;
import com.iamretailer.Common.LanguageList;
import com.iamretailer.Common.LocaleHelper;
import com.iamretailer.Common.RecyclerItemClickListener;
import com.iamretailer.POJO.OptionsPO;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.POJO.SingleOptionPO;
import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import stutzen.co.network.Connection;


public class ProductFullView extends Language {
    private LinearLayout ll_dots;
    LinearLayout buy;
    private DBController db;
    private TextView cart_count;
    private TextView p_name;
    private TextView productrate;
    private int check = 0;
    private FrameLayout error_network;
    private String p_id;
    private String product_id;
    private ArrayList<String> image_url;
    public ArrayList<String> zoom_url;
    private ArrayList<OptionsPO> optionsPOArrayList;
    private LinearLayout options;
    private String image;
    private WebView description;
    private int qty = 0;
    private FrameLayout loading;
    private TextView product;
    private TextView review;
    private FrameLayout review_layout;
    private FrameLayout option_layout;
    private ListView review_list;
    private TextView orginal;
    private TextView add_reviews;
    private FrameLayout add_rev_lay;
    private ImageView rate1;
    private ImageView rate2;
    private ImageView rate3;
    private ImageView rate4;
    private ImageView rate5;
    private int count = 0;
    private EditText comment;
    String prod_id = "";
    private LinearLayout review_section;
    private LinearLayout review_disp;
    private TextView no_reviews;
    private FrameLayout fullayout;
    private String pish;
    private String pas;
    private TextView errortxt1;
    private TextView errortxt2;
    private String cur_left = "";
    private String cur_right = "";
    LinearLayout loading_bar;
    private TextView out_of_stock;
    private ImageView like;
    private ImageView un_like;
    private AndroidLogger logger;
    private RecyclerView related_products;
    private FrameLayout related_view;
    private ArrayList<ProductsPO> list;
    private CommonAdapter adapter;
    private double sp_price;
    private double price;
    private TextView productrs;
    private TextView originalrs;
    private TextView productrs1;
    private TextView originalrs1;
    private ImageView r1;
    private ImageView r2;
    private ImageView r3;
    private ImageView r4;
    private ImageView r5;
    private Typeface typeface;
     private Typeface typeface1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonFunctions.updateAndroidSecurityProvider(this);
        db = new DBController(getApplicationContext());
        if (db.get_lan_c() > 0) {
            change_langs(db.get_lang_code());
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
           getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        }
        setContentView(R.layout.product_full_view);

        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        Bundle bundle = getIntent().getExtras();
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang = db.get_lang_code();
        Appconstatants.CUR=db.getCurCode();

        prod_id = bundle.getString("productid");
        pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/Heebo-Regular.ttf\")}body,li,p,span {font-family: MyFont;font-size: 14px;text-align: justify;color: #415163}</style></head><body>";
        pas = "</body></html>";
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);


        Log.d("Reviews_id", prod_id);
        productrs1= findViewById(R.id.productrs1);
        originalrs1= findViewById(R.id.originalrs1);

        p_name = findViewById(R.id.product_name);
        productrate = findViewById(R.id.productrate);
        buy = findViewById(R.id.buy);
        cart_count = findViewById(R.id.cart_count);
        LinearLayout menu = findViewById(R.id.menu);
        LinearLayout cart_items = findViewById(R.id.cart_items);
        LinearLayout share = findViewById(R.id.share);
        error_network = findViewById(R.id.error_network);
        options = findViewById(R.id.options);
        LinearLayout retry = findViewById(R.id.retry);
        description = findViewById(R.id.description);
        loading = findViewById(R.id.loading);
        product = findViewById(R.id.product);
        add_reviews = findViewById(R.id.add_reviews);
        loading_bar = findViewById(R.id.loading_bar);
        productrs= findViewById(R.id.productrs);
        originalrs= findViewById(R.id.originalrs);
        option_layout = findViewById(R.id.option_layout);
        review = findViewById(R.id.reviews);
        review_layout = findViewById(R.id.review_layout);
        review_list = findViewById(R.id.review_list);
        orginal = findViewById(R.id.orginal);
        add_rev_lay = findViewById(R.id.add_rev_lay);
        TextView view_all = findViewById(R.id.view_all);
        LinearLayout post_reviews = findViewById(R.id.post_reviews);
        comment = findViewById(R.id.comment);
        review_section = findViewById(R.id.review_section);
        review_disp = findViewById(R.id.review_disp);
        no_reviews = findViewById(R.id.no_review);
        fullayout = findViewById(R.id.fullayout);
        out_of_stock = findViewById(R.id.out_of_stock);
        like = findViewById(R.id.like);
        un_like = findViewById(R.id.unlike);
        FrameLayout fav = findViewById(R.id.fav);
        typeface=Typeface.createFromAsset(getAssets(),"font/Heebo-Medium.ttf");
        typeface1=Typeface.createFromAsset(getAssets(),"font/Heebo-Regular.ttf");
        cur_left = db.get_cur_Left();
        cur_right=db.get_cur_Right();
        related_products = findViewById(R.id.related_products);
        related_view = findViewById(R.id.related_view);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        r5 = findViewById(R.id.r5);


        rate1 = findViewById(R.id.rate1);
        rate2 = findViewById(R.id.rate2);
        rate3 = findViewById(R.id.rate3);
        rate4 = findViewById(R.id.rate4);
        rate5 = findViewById(R.id.rate5);


        rate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 1;

                rate1.setImageResource(R.mipmap.rating_fill);
                rate2.setImageResource(R.mipmap.rating_unfill);
                rate3.setImageResource(R.mipmap.rating_unfill);
                rate4.setImageResource(R.mipmap.rating_unfill);
                rate5.setImageResource(R.mipmap.rating_unfill);

            }
        });

        rate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 2;
                rate1.setImageResource(R.mipmap.rating_fill);
                rate2.setImageResource(R.mipmap.rating_fill);
                rate3.setImageResource(R.mipmap.rating_unfill);
                rate4.setImageResource(R.mipmap.rating_unfill);
                rate5.setImageResource(R.mipmap.rating_unfill);

            }
        });

        rate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 3;
                rate1.setImageResource(R.mipmap.rating_fill);
                rate2.setImageResource(R.mipmap.rating_fill);
                rate3.setImageResource(R.mipmap.rating_fill);
                rate4.setImageResource(R.mipmap.rating_unfill);
                rate5.setImageResource(R.mipmap.rating_unfill);
            }
        });

        rate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 4;
                rate1.setImageResource(R.mipmap.rating_fill);
                rate2.setImageResource(R.mipmap.rating_fill);
                rate3.setImageResource(R.mipmap.rating_fill);
                rate4.setImageResource(R.mipmap.rating_fill);
                rate5.setImageResource(R.mipmap.rating_unfill);

            }
        });

        rate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 5;
                rate1.setImageResource(R.mipmap.rating_fill);
                rate2.setImageResource(R.mipmap.rating_fill);
                rate3.setImageResource(R.mipmap.rating_fill);
                rate4.setImageResource(R.mipmap.rating_fill);
                rate5.setImageResource(R.mipmap.rating_fill);

            }
        });

        post_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0 && comment.getText().toString().length() == 0) {
                    Toast.makeText(ProductFullView.this, R.string.review_toast, Toast.LENGTH_SHORT).show();
                 } else {

                    POSTREVIEW postreview = new POSTREVIEW();
                    postreview.execute(db.getName(), comment.getText().toString().trim(), count + "");
                }

            }
        });


        Appconstatants.sessiondata = db.getSession();
        SingleProductTask singleProductTask = new SingleProductTask();
        singleProductTask.execute(Appconstatants.PRODUCT_LIST + prod_id);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_network.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);

                SingleProductTask singleProductTask = new SingleProductTask();
                singleProductTask.execute(Appconstatants.PRODUCT_LIST + prod_id);

            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       if (qty > 0) {
                                           CartSaveTask cartSaveTask = new CartSaveTask();
                                           cartSaveTask.execute();
                                       } else {
                                           Toast.makeText(ProductFullView.this, R.string.out_of_stock, Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               }
        );

        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductFullView.this, MyCart.class));
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareItem(image);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option_layout.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);

                if (review_layout.getVisibility() == View.VISIBLE)
                    review_layout.setVisibility(View.GONE);
                if (add_rev_lay.getVisibility() == View.VISIBLE)
                    add_rev_lay.setVisibility(View.GONE);


                product.setBackground(getResources().getDrawable(R.drawable.text_back));
                product.setTextColor(getResources().getColor(R.color.text_select));
                product.setTypeface(typeface);

                review.setBackground(getResources().getDrawable(R.color.white));
                review.setTextColor(getResources().getColor(R.color.un_select));
                review.setTypeface(typeface1);

                add_reviews.setBackground(getResources().getDrawable(R.color.white));
                add_reviews.setTextColor(getResources().getColor(R.color.un_select));
                add_reviews.setTypeface(typeface1);


            }
        });


        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review_layout.setVisibility(View.VISIBLE);

                if (option_layout.getVisibility() == View.VISIBLE)
                    option_layout.setVisibility(View.GONE);
                if (options.getVisibility() == View.VISIBLE)
                    options.setVisibility(View.GONE);
                if (add_rev_lay.getVisibility() == View.VISIBLE)
                    add_rev_lay.setVisibility(View.GONE);

                review.setBackground(getResources().getDrawable(R.drawable.text_back));
                review.setTextColor(getResources().getColor(R.color.text_select));
                review.setTypeface(typeface);

                product.setBackground(getResources().getDrawable(R.color.white));
                product.setTextColor(getResources().getColor(R.color.un_select));
                product.setTypeface(typeface1);

                add_reviews.setBackground(getResources().getDrawable(R.color.white));
                add_reviews.setTextColor(getResources().getColor(R.color.un_select));
                add_reviews.setTypeface(typeface1);


            }
        });

        add_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_rev_lay.setVisibility(View.VISIBLE);

                if (option_layout.getVisibility() == View.VISIBLE)
                    option_layout.setVisibility(View.GONE);
                if (options.getVisibility() == View.VISIBLE)
                    options.setVisibility(View.GONE);
                if (review_layout.getVisibility() == View.VISIBLE)
                    review_layout.setVisibility(View.GONE);

                add_reviews.setBackground(getResources().getDrawable(R.drawable.text_back));
                add_reviews.setTextColor(getResources().getColor(R.color.text_select));
                add_reviews.setTypeface(typeface);

                product.setBackground(getResources().getDrawable(R.color.white));
                product.setTextColor(getResources().getColor(R.color.un_select));
                product.setTypeface(typeface1);

                review.setBackground(getResources().getDrawable(R.color.white));
                review.setTextColor(getResources().getColor(R.color.un_select));
                review.setTypeface(typeface1);
            }
        });


        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductFullView.this, Review.class);
                Bundle nn = new Bundle();
                nn.putString("id", prod_id);
                intent.putExtras(nn);
                startActivity(intent);
            }
        });


        if (db.getLoginCount() > 0) {
            add_rev_lay.setVisibility(View.VISIBLE);
            add_reviews.setVisibility(View.VISIBLE);
            review_section.setVisibility(View.VISIBLE);

            if (option_layout.getVisibility() == View.VISIBLE)
                add_rev_lay.setVisibility(View.GONE);
            if (options.getVisibility() == View.VISIBLE)
                add_rev_lay.setVisibility(View.GONE);
            if (review_layout.getVisibility() == View.VISIBLE)
                add_rev_lay.setVisibility(View.GONE);


        } else {

            add_rev_lay.setVisibility(View.GONE);
            add_reviews.setVisibility(View.GONE);
            review_section.setVisibility(View.GONE);
        }

        product.performClick();

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.getLoginCount() > 0) {
                    if (un_like.getVisibility() == View.VISIBLE) {
                        un_like.setVisibility(View.GONE);
                        like.setVisibility(View.VISIBLE);
                        WishlistAddTask task = new WishlistAddTask();
                        task.execute(p_id);

                    } else {
                        un_like.setVisibility(View.VISIBLE);
                        like.setVisibility(View.GONE);
                        DeleteContactTask task = new DeleteContactTask();
                        task.execute(p_id);

                    }
                } else {
                    Intent intent = new Intent(ProductFullView.this, Login.class);
                    Bundle login = new Bundle();
                    login.putInt("from", 4);
                    intent.putExtras(login);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        if (db.getLoginCount() > 0) {
            add_rev_lay.setVisibility(View.VISIBLE);
            add_reviews.setVisibility(View.VISIBLE);
            review_section.setVisibility(View.VISIBLE);
            if (option_layout.getVisibility() == View.VISIBLE)
                add_rev_lay.setVisibility(View.GONE);
            if (options.getVisibility() == View.VISIBLE)
                add_rev_lay.setVisibility(View.GONE);
            if (review_layout.getVisibility() == View.VISIBLE)
                add_rev_lay.setVisibility(View.GONE);

        } else {
            add_rev_lay.setVisibility(View.GONE);
            add_reviews.setVisibility(View.GONE);
            review_section.setVisibility(View.GONE);
        }

        if (db.getLoginCount()>0) {

            WISH_LIST wish_list = new WISH_LIST();
            wish_list.execute(Appconstatants.Wishlist_Get);
        }

    }

    private void coloroption(final OptionsPO optionsPO, final int pos) {
        View layout = LayoutInflater.from(ProductFullView.this).inflate(R.layout.optionview, options, false);
        TextView heading = layout.findViewById(R.id.heading);
        String text=optionsPO.getName() + " :";
        heading.setText(text);
        final RecyclerView gridView = layout.findViewById(R.id.optiongrid);
        final ArrayList<SingleOptionPO> colorlist = optionsPO.getValuelist();
        if (colorlist.size() > 0) {
            colorlist.get(0).setImgSel(true);

            optionsPOArrayList.get(pos).setSelected_id(colorlist.get(0).getProduct_option_value_id());
            if (colorlist.get(0).getPrice()>0) {
                optionsPOArrayList.get(pos).setPrices(colorlist.get(0).getPrice());
                optionsPOArrayList.get(pos).setPrefix(colorlist.get(0).getPrefix());
            }
            ColorAdapters coladapter = new ColorAdapters(ProductFullView.this, colorlist);
            gridView.setAdapter(coladapter);
            gridView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        gridView.addOnItemTouchListener(new RecyclerItemClickListener(ProductFullView.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                for (int p = 0; p < colorlist.size(); p++) {
                    colorlist.get(p).setImgSel(false);
                }
                colorlist.get(i).setImgSel(true);

                final ColorAdapters coladapter = new ColorAdapters(ProductFullView.this, colorlist);
                gridView.setAdapter(coladapter);
                optionsPOArrayList.get(pos).setSelected_id(colorlist.get(i).getProduct_option_value_id());
                gridView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                if (colorlist.get(i).getPrice()>0)
                {
                    optionsPOArrayList.get(pos).setPrices(colorlist.get(i).getPrice());
                    optionsPOArrayList.get(pos).setPrefix(colorlist.get(i).getPrefix());
                    price_Cal();
                }
                else
                {
                    optionsPOArrayList.get(pos).setPrices(0);
                    optionsPOArrayList.get(pos).setPrefix("+");
                    price_Cal();

                }


            }
        }));


        options.addView(layout);
    }

    private void sizeoption(final OptionsPO optionsPO, final int pos) {
        View layout = LayoutInflater.from(ProductFullView.this).inflate(R.layout.sizeoption, options, false);
        final RecyclerView gridView = layout.findViewById(R.id.optiongrid);
        TextView heading = layout.findViewById(R.id.heading);
        String text=optionsPO.getName() + " :";
        heading.setText(text);
        final ArrayList<SingleOptionPO> sizelist = optionsPO.getValuelist();
        if (sizelist.size() > 0) {
            sizelist.get(0).setImgSel(true);
            optionsPOArrayList.get(pos).setSelected_id(sizelist.get(0).getProduct_option_value_id());
            if (sizelist.get(0).getPrice()>0) {
                optionsPOArrayList.get(pos).setPrices(sizelist.get(0).getPrice());
                optionsPOArrayList.get(pos).setPrefix(sizelist.get(0).getPrefix());
            }
            SizeOptionAdapters sizeadapter = new SizeOptionAdapters(ProductFullView.this, sizelist, 1);
            gridView.setAdapter(sizeadapter);
            gridView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }


        gridView.addOnItemTouchListener(new RecyclerItemClickListener(ProductFullView.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {

                for (int p = 0; p < sizelist.size(); p++) {
                    sizelist.get(p).setImgSel(false);
                }
                sizelist.get(i).setImgSel(true);
                optionsPOArrayList.get(pos).setSelected_id(sizelist.get(i).getProduct_option_value_id());
                final SizeOptionAdapters sizeadapter = new SizeOptionAdapters(ProductFullView.this, sizelist, 1);
                gridView.setAdapter(sizeadapter);
                gridView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                if (sizelist.get(i).getPrice()>0) {
                    optionsPOArrayList.get(pos).setPrices(sizelist.get(i).getPrice());
                    optionsPOArrayList.get(pos).setPrefix(sizelist.get(i).getPrefix());
                    price_Cal();
                }
                else
                {
                    optionsPOArrayList.get(pos).setPrices(0);
                    optionsPOArrayList.get(pos).setPrefix("+");
                    price_Cal();
                }

            }
        }));

        options.addView(layout);
    }

    private void weightoption(final OptionsPO optionsPO, final int pos) {
        View layout = LayoutInflater.from(ProductFullView.this).inflate(R.layout.weightoption, options, false);
        final Spinner weight_spinner = layout.findViewById(R.id.weight_spinner);
        final ArrayList<SingleOptionPO> spinner_string = optionsPO.getValuelist();
        TextView heading = layout.findViewById(R.id.heading);
        String text=optionsPO.getName() + " :";
        heading.setText(text);
        Log.i("tag", "weight" + optionsPO.getValuelist());
        if (spinner_string != null && spinner_string.size() > 0) {
            SpinnerAdapter adapter1 = new SpinnerAdapter(ProductFullView.this, spinner_string, 0);
            weight_spinner.setAdapter(adapter1);
        }
        weight_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_string!=null) {
                    optionsPOArrayList.get(pos).setSelected_id(spinner_string.get(i).getProduct_option_value_id());
                    if (spinner_string.get(i).getPrice() > 0) {
                        optionsPOArrayList.get(pos).setPrices(spinner_string.get(i).getPrice());
                        optionsPOArrayList.get(pos).setPrefix(spinner_string.get(i).getPrefix());
                        price_Cal();
                    } else {
                        optionsPOArrayList.get(pos).setPrices(0);
                        optionsPOArrayList.get(pos).setPrefix("+");
                        price_Cal();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        options.addView(layout);
    }

    private void checkbox_option(final OptionsPO optionsPO, final int pos) {
        View layout = LayoutInflater.from(ProductFullView.this).inflate(R.layout.check_box_option, options, false);
        TextView heading = layout.findViewById(R.id.heading);
        final RadioGroup checkbox1 = layout.findViewById(R.id.checkbox);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        String text=optionsPO.getName() + " :";
        heading.setText(text);
        final ArrayList<SingleOptionPO> check_value = optionsPO.getValuelist();

        for (int u = 0; u < check_value.size(); u++) {
            RadioButton rbn = new RadioButton(ProductFullView.this);
            rbn.setId(check_value.get(u).getProduct_option_value_id());
            String value="  " + check_value.get(u).getName();
            rbn.setText(value);
            rbn.setTypeface(typeface);
            rbn.setTextColor(getResources().getColor(R.color.text_select));
            rbn.setButtonDrawable(getResources().getDrawable(R.drawable.raidobuttonstyle));
            if (u==0) {
                rbn.setChecked(true);
                optionsPOArrayList.get(pos).setSelected_id(check_value.get(u).getProduct_option_value_id());
                optionsPOArrayList.get(pos).setPrices(check_value.get(u).getPrice());
                optionsPOArrayList.get(pos).setPrefix(check_value.get(u).getPrefix());
            }
            else {
                rbn.setChecked(false);
            }
            rbn.setLayoutParams(params);
            rbn.setTag(u);
            checkbox1.addView(rbn);
        }

        checkbox1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = checkbox1.getCheckedRadioButtonId();
                optionsPOArrayList.get(pos).setSelected_id(selectedId);
                RadioButton radioButton = group.findViewById(checkedId);
                int mySelectedIndex = (int) radioButton.getTag();
                Log.d("dsadad",mySelectedIndex+"");
                if (check_value.get(mySelectedIndex).getPrice()>0) {
                    optionsPOArrayList.get(pos).setPrices(check_value.get(mySelectedIndex).getPrice());
                    optionsPOArrayList.get(pos).setPrefix(check_value.get(mySelectedIndex).getPrefix());
                    price_Cal();
                }
                else
                {
                    optionsPOArrayList.get(pos).setPrices(0);
                    optionsPOArrayList.get(pos).setPrefix("+");
                    price_Cal();
                }

            }
        });


        options.addView(layout);
    }

    private void radio_button(final OptionsPO optionsPO, final int pos) {

        View layout = LayoutInflater.from(ProductFullView.this).inflate(R.layout.radio_option, options, false);
        final RadioGroup radio = layout.findViewById(R.id.radio);
        TextView heading = layout.findViewById(R.id.heading);
        String text=optionsPO.getName() + " :";
        heading.setText(text);
        final ArrayList<SingleOptionPO> radio_values = optionsPO.getValuelist();
        for (int u = 0; u < radio_values.size(); u++) {
            RadioButton rbn = new RadioButton(ProductFullView.this);
            Log.i("hishihfi","rrrrrr");
            rbn.setButtonDrawable(getResources().getDrawable(R.drawable.radiobutton_selector));
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rbn.setTypeface(typeface);
            rbn.setLayoutParams(params);
            rbn.setPadding((int) getApplicationContext().getResources().getDimension(R.dimen.dp15),(int) getApplicationContext().getResources().getDimension(R.dimen.dp15),(int) getApplicationContext().getResources().getDimension(R.dimen.dp15),(int) getApplicationContext().getResources().getDimension(R.dimen.dp15));

            rbn.setId(radio_values.get(u).getProduct_option_value_id());
            rbn.setText(radio_values.get(u).getName());
            rbn.setTextColor(getResources().getColor(R.color.text_select));



            if (u==0)
            {

                rbn.setChecked(true);
                optionsPOArrayList.get(pos).setSelected_id(radio_values.get(u).getProduct_option_value_id());
                optionsPOArrayList.get(pos).setPrices(radio_values.get(u).getPrice());
                optionsPOArrayList.get(pos).setPrefix(radio_values.get(u).getPrefix());
            }
            else
            {
                rbn.setChecked(false);

            }
            rbn.setTypeface(typeface);
            rbn.setTag(u);
            radio.addView(rbn);
        }
        options.addView(layout);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radio.getCheckedRadioButtonId();
                optionsPOArrayList.get(pos).setSelected_id(selectedId);
                RadioButton radioButton = arg0.findViewById(arg1);
                int mySelectedIndex = (int) radioButton.getTag();
                if (radio_values.get(mySelectedIndex).getPrice()>0) {
                    optionsPOArrayList.get(pos).setPrices(radio_values.get(mySelectedIndex).getPrice());
                    optionsPOArrayList.get(pos).setPrefix(radio_values.get(mySelectedIndex).getPrefix());
                    price_Cal();
                }else
                {
                    optionsPOArrayList.get(pos).setPrices(0);
                    optionsPOArrayList.get(pos).setPrefix("+");
                    price_Cal();

                }

            }
        });


    }


    private void init() {

        ViewPager viewPager = findViewById(R.id.viewPager);
        ll_dots = findViewById(R.id.indicator);
        SliderAdapter sliderAdapter = new SliderAdapter(ProductFullView.this, image_url);
        viewPager.setAdapter(sliderAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        ImageView[] dots = new ImageView[image_url.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.mipmap.dot_un_fill);
            dots[i].setPadding(0, 0, 5, 0);
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setImageResource(R.mipmap.dot_fill);
    }

    private class SingleProductTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
        }
        protected String doInBackground(String... param) {

            Log.d("singleurl", param[0]);
            logger.info("Single product api" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR,ProductFullView.this);
                logger.info("Single product resp" + response);
                Log.d("products_vapi", param[0]);
                Log.d("products_vv", response);
                Log.d("products_ses", Appconstatants.sessiondata);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "SingleProducts--->  " + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    image_url = new ArrayList<>();
                    zoom_url = new ArrayList<>();

                    optionsPOArrayList = new ArrayList<>();
                    if (json.getInt("success") == 1) {
                        JSONObject jsonObject = new JSONObject(json.getString("data"));
                        p_id = jsonObject.isNull("id") ? "" : jsonObject.getString("id");
                        product_id = jsonObject.isNull("product_id") ? "" : jsonObject.getString("product_id");
                        p_name.setText(jsonObject.isNull("name") ? "" : jsonObject.getString("name"));

                        image = jsonObject.getString("image");
                        qty = jsonObject.isNull("quantity") ? 0 : jsonObject.getInt("quantity");
                        boolean wish_list = !jsonObject.isNull("wish_list") && jsonObject.getBoolean("wish_list");

                        if (wish_list) {
                            un_like.setVisibility(View.GONE);
                            like.setVisibility(View.VISIBLE);
                        } else {
                            un_like.setVisibility(View.VISIBLE);
                            like.setVisibility(View.GONE);
                        }
                        double rating=jsonObject.isNull("rating") ? 0 : jsonObject.getDouble("rating");

                        if (rating == 0) {
                            r1.setImageResource(R.mipmap.star_unfill);
                            r2.setImageResource(R.mipmap.star_unfill);
                            r3.setImageResource(R.mipmap.star_unfill);
                            r4.setImageResource(R.mipmap.star_unfill);
                            r5.setImageResource(R.mipmap.star_unfill);
                        } else if (rating == 1) {

                            r1.setImageResource(R.mipmap.star_fill);
                            r2.setImageResource(R.mipmap.star_unfill);
                            r3.setImageResource(R.mipmap.star_unfill);
                            r4.setImageResource(R.mipmap.star_unfill);
                            r5.setImageResource(R.mipmap.star_unfill);
                        } else if (rating == 2) {
                            r1.setImageResource(R.mipmap.star_fill);
                            r2.setImageResource(R.mipmap.star_fill);
                            r3.setImageResource(R.mipmap.star_unfill);
                            r4.setImageResource(R.mipmap.star_unfill);
                            r5.setImageResource(R.mipmap.star_unfill);
                        } else if (rating == 3) {
                            r1.setImageResource(R.mipmap.star_fill);
                            r2.setImageResource(R.mipmap.star_fill);
                            r3.setImageResource(R.mipmap.star_fill);
                            r4.setImageResource(R.mipmap.star_unfill);
                            r5.setImageResource(R.mipmap.star_unfill);
                        } else if (rating == 4) {
                            r1.setImageResource(R.mipmap.star_fill);
                            r2.setImageResource(R.mipmap.star_fill);
                            r3.setImageResource(R.mipmap.star_fill);
                            r4.setImageResource(R.mipmap.star_fill);
                            r5.setImageResource(R.mipmap.star_unfill);
                        } else if (rating == 5) {
                            r1.setImageResource(R.mipmap.star_fill);
                            r2.setImageResource(R.mipmap.star_fill);
                            r3.setImageResource(R.mipmap.star_fill);
                            r4.setImageResource(R.mipmap.star_fill);
                            r5.setImageResource(R.mipmap.star_fill);
                        } else {
                            r1.setImageResource(R.mipmap.star_unfill);
                            r2.setImageResource(R.mipmap.star_unfill);
                            r3.setImageResource(R.mipmap.star_unfill);
                            r4.setImageResource(R.mipmap.star_unfill);
                            r5.setImageResource(R.mipmap.star_unfill);
                        }
                        Log.d("quantity_aa", qty + "");
                        JSONArray slide_img = new JSONArray(jsonObject.getString("images"));

                        if (qty > 0) {
                            out_of_stock.setVisibility(View.GONE);
                        } else {
                            out_of_stock.setVisibility(View.VISIBLE);
                        }

                        if (slide_img.length() != 0) {
                            for (int a = 0; a < slide_img.length(); a++) {
                                image_url.add(slide_img.getString(a));
                            }

                        } else {
                            image_url.add(image);
                        }

                        JSONArray zoom_img = new JSONArray(jsonObject.getString("original_images"));

                        if (zoom_img.length() == 0) {
                            zoom_url.add(image);
                        } else {
                            for (int b = 0; b < zoom_img.length(); b++) {
                                zoom_url.add(zoom_img.getString(b));
                            }
                        }

                        init();
                        addBottomDots(0);

                        price = jsonObject.isNull("price") ? 0.00 : jsonObject.getDouble("price");
                        sp_price=jsonObject.isNull("special") ? 0.00 : jsonObject.getDouble("special");
                        productrs.setText(cur_left);
                        productrs1.setText(cur_right);
                        originalrs.setText(cur_left);
                        originalrs1.setText(cur_right);
                        if (jsonObject.getDouble("special") > 0) {
                            String val=String.format("%.2f", sp_price);
                            productrate.setText(val);
                            String val1=String.format("%.2f", price);
                            orginal.setText(val1);

                        } else {
                            String val=String.format("%.2f", price);
                            productrate.setText(val);
                            orginal.setVisibility(View.GONE);
                            originalrs.setVisibility(View.GONE);
                            originalrs1.setVisibility(View.GONE);
                        }

                        String myHtmlString = pish + (jsonObject.isNull("description") ? "" : jsonObject.getString("description")) + pas;

                        description.loadDataWithBaseURL(null, myHtmlString, "text/html", "utf-8", null);
                        description.setBackgroundColor(getResources().getColor(R.color.white));
                        JSONArray option_array = new JSONArray(jsonObject.getString("options"));
                        for (int y = 0; y < option_array.length(); y++) {
                            JSONObject jsonObject1 = option_array.getJSONObject(y);
                            OptionsPO optionsPO = new OptionsPO();
                            optionsPO.setProduct_option_id(jsonObject1.getInt("product_option_id"));
                            optionsPO.setName(jsonObject1.isNull("name") ? "" : jsonObject1.getString("name"));
                            optionsPO.setOption_id(jsonObject1.getInt("option_id"));
                            optionsPO.setType(jsonObject1.isNull("type") ? "" : jsonObject1.getString("type"));
                            optionsPO.setValue(jsonObject1.isNull("value") ? "" : jsonObject1.getString("value"));
                            optionsPO.setRequired(jsonObject1.isNull("required") ? "" : jsonObject1.getString("required"));
                            JSONArray jsonArray = jsonObject1.getJSONArray("option_value");
                            ArrayList<SingleOptionPO> op_list = new ArrayList<>();
                            for (int u = 0; u < jsonArray.length(); u++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(u);
                                SingleOptionPO single = new SingleOptionPO();
                                single.setImga_url(jsonObject2.isNull("image") ? "" : jsonObject2.getString("image"));
                                single.setPrice(jsonObject2.isNull("price") ?0:jsonObject2.getDouble("price"));
                                single.setName(jsonObject2.isNull("name") ? "" : jsonObject2.getString("name"));
                                single.setOption_value_id(jsonObject2.getInt("option_value_id"));
                                single.setProduct_option_value_id(jsonObject2.getInt("product_option_value_id"));
                                single.setPrefix(jsonObject2.isNull("price_prefix") ? "" : jsonObject2.getString("price_prefix"));
                                single.setQuantity(jsonObject2.getInt("quantity"));
                                single.setType(jsonObject2.isNull("type") ? "" : jsonObject2.getString("type"));
                                op_list.add(single);
                            }
                            optionsPO.setValuelist(op_list);
                            optionsPOArrayList.add(optionsPO);

                        }
                        if (optionsPOArrayList != null && optionsPOArrayList.size() > 0) {
                                DrawOptions();
                        }

                        JSONObject rev_obj = new JSONObject(jsonObject.getString("reviews"));

                        if (Integer.parseInt(rev_obj.getString("review_total")) != 0) {

                            ArrayList<SingleOptionPO> rev_list = new ArrayList<>();
                            JSONArray array = rev_obj.getJSONArray("reviews");

                            Log.d("size_check", array.length() + "");

                            if (array.length() < 5) {

                                for (int u = 0; u < array.length(); u++) {
                                    JSONObject object = array.getJSONObject(u);
                                    SingleOptionPO bo = new SingleOptionPO();
                                    bo.setRev_author(object.isNull("author") ? "" : object.getString("author"));
                                    bo.setRev_text(object.isNull("text") ? "" : object.getString("text"));
                                    bo.setRev_date(object.isNull("date_added") ? "" : object.getString("date_added"));
                                    bo.setRating(object.isNull("rating") ? 0 : object.getInt("rating"));
                                    rev_list.add(bo);

                                }
                            } else {
                                for (int u = 0; u < 5; u++) {
                                    JSONObject object = array.getJSONObject(u);
                                    SingleOptionPO bo = new SingleOptionPO();
                                    bo.setRev_author(object.isNull("author") ? "" : object.getString("author"));
                                    bo.setRev_text(object.isNull("text") ? "" : object.getString("text"));
                                    bo.setRev_date(object.isNull("date_added") ? "" : object.getString("date_added"));
                                    bo.setRating(object.isNull("rating") ? 0 : object.getInt("rating"));
                                    rev_list.add(bo);

                                }
                            }
                            ReviewAdapter reviewAdapter = new ReviewAdapter(ProductFullView.this, R.layout.review_list, rev_list);
                            review_list.setAdapter(reviewAdapter);
                            Helper.getListViewSize(review_list);
                            review_disp.setVisibility(View.VISIBLE);
                            no_reviews.setVisibility(View.GONE);


                        } else {
                            review_disp.setVisibility(View.GONE);
                            no_reviews.setVisibility(View.VISIBLE);
                        }

                        RELATED_PRODUCTS related_products = new RELATED_PRODUCTS();
                        related_products.execute(Appconstatants.RELATED_API + prod_id);


                    } else {
                        loading.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        error_network.setVisibility(View.VISIBLE);
                        JSONArray array = json.getJSONArray("error");
                        String error=array.getString(0) + "";
                        errortxt2.setText(error);
                        Toast.makeText(ProductFullView.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    error_network.setVisibility(View.GONE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    SingleProductTask singleProductTask = new SingleProductTask();
                                    singleProductTask.execute(Appconstatants.PRODUCT_LIST + prod_id);

                                }
                            })
                            .show();
                }
            } else {

                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

            }


        }
    }

    private void DrawOptions() {
        options.removeAllViews();
        for (int i = 0; i < optionsPOArrayList.size(); i++) {
            Log.i("inputtt", optionsPOArrayList.get(i).getName() + "---" + optionsPOArrayList.get(i).getType());
            if ((optionsPOArrayList.get(i).getName().equalsIgnoreCase(getResources().getString(R.string.colrs)) || optionsPOArrayList.get(i).getName().equalsIgnoreCase(getResources().getString(R.string.colss))) && optionsPOArrayList.get(i).getType().equalsIgnoreCase(getResources().getString(R.string.radio))) {
                Log.i("input_color", optionsPOArrayList.get(i).getName() + "---" + optionsPOArrayList.get(i).getValuelist().size());
                coloroption(optionsPOArrayList.get(i), i);
            } else if (optionsPOArrayList.get(i).getName().equalsIgnoreCase(getResources().getString(R.string.size))) {
                Log.i("input_size", optionsPOArrayList.get(i).getName() + "---" + optionsPOArrayList.get(i).getValuelist().size());
                sizeoption(optionsPOArrayList.get(i), i);
            } else if (optionsPOArrayList.get(i).getType().equalsIgnoreCase(getResources().getString(R.string.chelk_box))) {
                checkbox_option(optionsPOArrayList.get(i), i);
            } else if (optionsPOArrayList.get(i).getType().equalsIgnoreCase(getResources().getString(R.string.radio))) {
                radio_button(optionsPOArrayList.get(i), i);
            } else {
                Log.i("inputtt", optionsPOArrayList.get(i).getName() + "---" + optionsPOArrayList.get(i).getValuelist().size());
                weightoption(optionsPOArrayList.get(i), i);

            }

        }
    }

    private class CartSaveTask extends AsyncTask<Object, Void, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ProductFullView.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("Cart", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("Cart Save api" + Appconstatants.cart_api);

            String response = null;
            Connection connection = new Connection();
            try {


                JSONObject json = new JSONObject();
                json.put("product_id", product_id);
                json.put("quantity", "1");


                JSONObject object = new JSONObject();
                Log.d("Cart_S", String.valueOf(optionsPOArrayList.size()));


                for (int y = 0; y < optionsPOArrayList.size(); y++) {
                    Log.i("optionval", optionsPOArrayList.size() + "-" + optionsPOArrayList.get(y).getProduct_option_id() + "");
                    try {
                        object.put(String.valueOf(optionsPOArrayList.get(y).getProduct_option_id()), optionsPOArrayList.get(y).getSelected_id());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }


                json.put("option", object);
                Log.d("Cart_input", json.toString());
                Log.d("Cart_url_insert", Appconstatants.sessiondata + "");
                logger.info("Cart Save req" + json);
                response = connection.sendHttpPostjson(Appconstatants.cart_api, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR,ProductFullView.this);
                logger.info("Cart Save resp" + response);


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Cart", "Cart_resp  " + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.cart_Add_text), Toast.LENGTH_SHORT).show();

                        cart_call();

                    } else {

                        JSONArray error = json.getJSONArray("error");
                        String error_msg = error.getString(0);
                        Toast.makeText(ProductFullView.this, error_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg,
                            Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buy.performClick();
                        }
                    })
                            .show();
                    Toast.makeText(ProductFullView.this, R.string.error_msg, Toast.LENGTH_SHORT).show();

                }
            } else {
                Snackbar.make(fullayout, R.string.error_net,
                        Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buy.performClick();
                    }
                }).show();
                Toast.makeText(ProductFullView.this, R.string.error_net, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class CartTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

            Log.d("Cart_list", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Cart api:" + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata + "");
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR,ProductFullView.this);
                logger.info("Cart resp" + response);
                Log.d("Cart_list_resp", response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Cart_list_resp", "CartResp--->  " + resp);

            if (resp != null) {

                try {
                    ArrayList<ProductsPO> cart_item = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONArray) {
                            cart_count.setText(String.valueOf(0));


                        } else if (dd instanceof JSONObject) {
                            JSONObject jsonObject = new JSONObject(json.getString("data"));
                            JSONArray array = new JSONArray(jsonObject.getString("products"));
                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(jsonObject1.isNull("product_id") ? "" : jsonObject1.getString("product_id"));
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                                cart_item.add(bo);
                            }
                            cart_count.setText(String.valueOf(qty));
                            if (list!=null&&list.size()>0 ) {
                                for (int u = 0; u < list.size(); u++) {
                                    for (int h = 0; h < cart_item.size(); h++) {
                                        if (Integer.parseInt(list.get(u).getProduct_id())==Integer.parseInt(cart_item.get(h).getProduct_id())) {
                                            list.get(u).setCart_list(true);
                                            break;
                                        }
                                        else
                                        {
                                            list.get(u).setCart_list(false);
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                        }
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(ProductFullView.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                }
            }

        }
    }


    private void shareItem(String url) {
        Log.d("Image_share", url);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        final PackageManager pm = getPackageManager();
        final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        try {
            Picasso.with(getApplicationContext()).load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Intent i = new Intent(Intent.ACTION_SEND);

                    for (ApplicationInfo packageInfo : packages) {
                        Log.d("app_1", "Installed package :" + packageInfo.packageName);
                        if (packageInfo.packageName.equalsIgnoreCase("com.whatsapp")) {
                            i.setPackage("com.whatsapp");
                            i.putExtra(Intent.EXTRA_TEXT, p_name.getText().toString() + "\nPrice - " + productrate.getText().toString() + "\nFrom-" + "\n" + Appconstatants.domain);
                            i.setType("image/*");
                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri2(bitmap));
                            startActivity(Intent.createChooser(i, "Share"));
                            check++;
                        } else if (packageInfo.packageName.equalsIgnoreCase("com.whatsapp.w4b")) {
                            i.setPackage("com.whatsapp.w4b");
                            i.putExtra(Intent.EXTRA_TEXT, p_name.getText().toString() + "\nPrice - " + productrate.getText().toString() + "\nFrom-" + "\n" + Appconstatants.domain);
                            i.setType("image/*");
                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri2(bitmap));
                            startActivity(Intent.createChooser(i, "Share"));
                            check++;
                        }


                    }


                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
            Log.d("Values_", check + "");
            if (check == 0) {
                Toast.makeText(ProductFullView.this, R.string.no_whats, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(ProductFullView.this, R.string.app_not, Toast.LENGTH_LONG).show();
        }

    }


    private Uri getLocalBitmapUri2(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    private class POSTREVIEW extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            Log.d("Login", "started");


            pDialog = new ProgressDialog(ProductFullView.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {
            logger.info("Post Review api" + Appconstatants.POSTREVIEW + prod_id);
            Log.i("Post Review api", Appconstatants.POSTREVIEW + prod_id);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("name", param[0]);
                json.put("text", param[1]);
                json.put("rating", param[2]);
                Log.d("Reviews_post", json.toString());
                Log.d("Reviews_url", Appconstatants.POSTREVIEW + prod_id);

                logger.info("Post Review req" + json);
                Log.i("tag", "login" + Appconstatants.sessiondata);
                response = connection.sendHttpPostjson(Appconstatants.POSTREVIEW + prod_id, json, db.getSession(), Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR,ProductFullView.this);
                logger.info("Post Review resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Login", "Login--?" + resp);
            Log.d("login_ss", resp + "");
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Toast.makeText(ProductFullView.this, R.string.post_suc, Toast.LENGTH_SHORT).show();
                        rate1.setImageResource(R.mipmap.un_fill);
                        rate2.setImageResource(R.mipmap.un_fill);
                        rate3.setImageResource(R.mipmap.un_fill);
                        rate4.setImageResource(R.mipmap.un_fill);
                        rate5.setImageResource(R.mipmap.un_fill);
                        count = 0;
                        comment.setText("");
                        // SingleProductTask singleProductTask = new SingleProductTask();
                        // singleProductTask.execute(Appconstatants.PRODUCT_LIST + prod_id);
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(ProductFullView.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ProductFullView.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(ProductFullView.this, R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class WishlistAddTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        protected String doInBackground(String... param) {
            logger.info("Add wish list api" + Appconstatants.WishList_Add + param[0]);

            String response = null;
            Connection connection = new Connection();
            try {

                response = connection.sendHttpPostjson(Appconstatants.WishList_Add + param[0], null, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR,ProductFullView.this);
                logger.info("Add wish list resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {

            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        un_like.setVisibility(View.GONE);
                        like.setVisibility(View.VISIBLE);
                        Toast.makeText(ProductFullView.this, R.string.wish_add, Toast.LENGTH_SHORT).show();

                    } else {
                        un_like.setVisibility(View.VISIBLE);
                        like.setVisibility(View.GONE);
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(ProductFullView.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    un_like.setVisibility(View.VISIBLE);
                    like.setVisibility(View.GONE);
                    e.printStackTrace();
                    Toast.makeText(ProductFullView.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                un_like.setVisibility(View.VISIBLE);
                like.setVisibility(View.GONE);
                Toast.makeText(ProductFullView.this, R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DeleteContactTask extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... param) {
            logger.info("Delete wish List api" + Appconstatants.WishList_Add + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();

                response = connection.sendHttpDelete(Appconstatants.WishList_Add + param[0], null, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR,ProductFullView.this);
                logger.info("Delete wish List resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;

        }

        protected void onPostExecute(String res) {
            Log.i("resd", res + "");
            if (res != null) {
                try {
                    JSONObject json = new JSONObject(res);
                    if (json.getInt("success") == 1) {
                        like.setVisibility(View.GONE);
                        un_like.setVisibility(View.VISIBLE);
                        Toast.makeText(ProductFullView.this, R.string.dele_wish, Toast.LENGTH_SHORT).show();

                    } else {
                        un_like.setVisibility(View.GONE);
                        like.setVisibility(View.VISIBLE);
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(ProductFullView.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    un_like.setVisibility(View.GONE);
                    like.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                    Toast.makeText(ProductFullView.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }

            } else {
                un_like.setVisibility(View.GONE);
                like.setVisibility(View.VISIBLE);
                Toast.makeText(ProductFullView.this, R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class RELATED_PRODUCTS extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Best selling api:" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR,ProductFullView.this);
                logger.info("Best selling api:" + response);
                Log.d("prducts_api", param[0]);
                Log.d("prducts_", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            if (resp != null) {
                try {
                    list = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {

                        if (json.has("data")) {
                            JSONArray arr = new JSONArray(json.getString("data"));

                            if (arr.length() > 0) {
                                for (int h = 0; h < arr.length(); h++) {
                                    JSONObject obj = arr.getJSONObject(h);
                                    ProductsPO bo = new ProductsPO();
                                    bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                    bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                                    bo.setProd_original_rate(Double.parseDouble(obj.isNull("price") ? "" : obj.getString("price")));
                                    bo.setProd_offer_rate(Double.parseDouble(obj.isNull("special") ? "" : obj.getString("special")));
                                    bo.setProducturl(obj.isNull("thumb") ? "" : obj.getString("thumb"));
                                    bo.setQty(obj.isNull("quantity") ? 0 : obj.getInt("quantity"));
                                    bo.setP_rate(obj.isNull("rating") ? 0 : obj.getDouble("rating"));
                                    bo.setWish_list(false);
                                    bo.setCart_list(false);
                                    Object dd = obj.get("option");
                                    ArrayList<SingleOptionPO> optionPOS = new ArrayList<>();
                                    if (dd instanceof JSONObject)
                                    {
                                        JSONObject jsonObject=obj.getJSONObject("option");
                                        JSONArray option=jsonObject.getJSONArray("product_option_value");
                                        if (option.length()>0) {


                                            for (int k = 0; k < option.length(); k++) {
                                                JSONObject object1 = option.getJSONObject(k);
                                                SingleOptionPO po = new SingleOptionPO();
                                                po.setProduct_option_value_id(object1.isNull("product_option_value_id") ? 0 : object1.getInt("product_option_value_id"));
                                                optionPOS.add(po);

                                            }
                                            bo.setSingleOptionPOS(optionPOS);
                                        }
                                    }
                                    else
                                    {
                                        bo.setSingleOptionPOS(optionPOS);
                                    }
                                    list.add(bo);
                                }
                            }
                            if (list.size() != 0) {
                                adapter = new CommonAdapter(ProductFullView.this, list,1,6);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                related_products.setLayoutManager(layoutManager);
                                related_products.setAdapter(adapter);
                                related_view.setVisibility(View.VISIBLE);

                            } else {
                                related_view.setVisibility(View.GONE);
                            }
                        } else {
                            related_view.setVisibility(View.GONE);
                        }
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);
                        CartTask cartTask = new CartTask();
                        cartTask.execute(Appconstatants.cart_api);
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(ProductFullView.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    SingleProductTask singleProductTask = new SingleProductTask();
                                    singleProductTask.execute(Appconstatants.PRODUCT_LIST + prod_id);

                                }
                            })
                            .show();

                }
            } else {
                loading_bar.setVisibility(View.GONE);
                Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loading_bar.setVisibility(View.VISIBLE);
                                SingleProductTask singleProductTask = new SingleProductTask();
                                singleProductTask.execute(Appconstatants.PRODUCT_LIST + prod_id);

                            }
                        })
                        .show();

            }
        }
    }

    private void change_langs(String languageToLoad) {

        ArrayList<String> lang_list = LanguageList.getLang_list();
        String set_lan = "en";

        for (int h = 0; h < lang_list.size(); h++) {
            if (languageToLoad.contains(lang_list.get(h))) {
                set_lan = lang_list.get(h);

            }

        }
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            // API 17+ only.
            Locale locale = new Locale(set_lan);
            Locale.setDefault(locale);
            config.locale = locale;

        } else {
            config.locale = new Locale("en");
        }

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }


    private void cart_call()
    {
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }

    private class WISH_LIST extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("WIsh list api"+param[0]);


            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,ProductFullView.this);
                logger.info("WIsh list api resp"+response);
                Log.d("wish_api", param[0]);
                Log.d("wish_res", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            if (resp != null) {
                try {
                    ArrayList<ProductsPO> fav_item = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        JSONArray array = json.getJSONArray("data");
                        Log.d("wish_res", "ddsadsa");

                        if (array.length() > 0) {
                            for (int h = 0; h < array.length(); h++) {
                                JSONObject obj = array.getJSONObject(h);
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                fav_item.add(bo);
                            }

                            if ( list!=null&&list.size()>0 ) {
                                for (int u = 0; u < list.size(); u++) {
                                    for (int h = 0; h < fav_item.size(); h++) {
                                        if (Integer.parseInt(list.get(u).getProduct_id())==Integer.parseInt(fav_item.get(h).getProduct_id())) {
                                            list.get(u).setWish_list(true);
                                            break;
                                        }
                                        else
                                        {
                                            list.get(u).setWish_list(false);
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void price_Cal()
    {
            double price1 = 0;
            double price2 = 0;
             double whole_price1=0;
            double whole_price2=0;
            for (int y = 0; y < optionsPOArrayList.size(); y++) {
                Log.i("optionval", optionsPOArrayList.size() + "-" + optionsPOArrayList.get(y).getProduct_option_id() + "");
                // whole=whole+optionsPOArrayList.get(y).getPrices();
                Log.d("adsadda", optionsPOArrayList.get(y).getPrefix() + "");
                Log.d("adsadda", optionsPOArrayList.get(y).getPrices() + "");
                if (optionsPOArrayList.get(y).getPrices()>0) {
                    if (sp_price > 0) {
                        if (optionsPOArrayList.get(y).getPrefix().equalsIgnoreCase("-")) {
                            price1 = price1 - optionsPOArrayList.get(y).getPrices();
                            price2 = price2 - optionsPOArrayList.get(y).getPrices();
                        } else {
                            price1 = price1 + optionsPOArrayList.get(y).getPrices();
                            price2 = price2 + optionsPOArrayList.get(y).getPrices();

                        }
                    } else {

                        if (optionsPOArrayList.get(y).getPrefix().equalsIgnoreCase("-")) {
                            price2 = price2 + optionsPOArrayList.get(y).getPrices();
                        } else {
                            price2 = price2 + optionsPOArrayList.get(y).getPrices();
                        }
                    }
                }
                else
                {
                    Log.d("adsadda", optionsPOArrayList.get(y).getPrices() + "");
                    if (sp_price > 0) {
                            price1 = 0;
                            price2 = 0;
                    } else {
                            price2 = 0;
                    }
                }

            }
            if (sp_price>0) {
                whole_price1=whole_price1+price1 + sp_price;
                whole_price2=whole_price2+price2 + price;
                String wp_val=String.format("%.2f", whole_price1);
                String wp_val2=String.format("%.2f", whole_price2);
                productrate.setText(wp_val);
                orginal.setText(wp_val2);
            }
            else
            {
               whole_price2=whole_price2+price2 + price;
                String val=String.format("%.2f", whole_price2);
                productrate.setText(val);
                orginal.setVisibility(View.GONE);

            }

    }

    public void cart_inc()
    {
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }
}
