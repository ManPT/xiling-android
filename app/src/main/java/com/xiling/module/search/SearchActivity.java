package com.xiling.module.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.sobot.chat.utils.ToastUtil;
import com.xiling.R;
import com.xiling.ddui.custom.DDDeleteDialog;
import com.xiling.ddui.custom.ScreeningView;
import com.xiling.ddui.fragment.ShopFragment;
import com.xiling.ddui.manager.DDSearchWordManager;
import com.xiling.module.auth.Config;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.DDHomeService;
import com.xiling.shared.util.ConvertUtil;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {


    @BindView(R.id.keywordsLayout)
    protected ScrollView mKeywordsLayout;

    @BindView(R.id.hotKeywordsLayout)
    protected FlexboxLayout mHotKeywordsLayout;

    @BindView(R.id.cancelBtn)
    protected TextView mCancelBtn;
    @BindView(R.id.cleanBtn)
    protected ImageView mCleanBtn;
    @BindView(R.id.keywordEt)
    protected EditText mKeywordEt;

    @BindView(R.id.responseView)
    LinearLayout responseView;

    @BindView(R.id.deleteHistoryButton)
    ImageView deleteHistoryButton;
    @BindView(R.id.screenView)
    ScreeningView screenView;

    private String mKeyword = "";
    int mType;
    DDHomeService homeService = null;

    DDSearchWordManager searchWordManager = DDSearchWordManager.share();
    ShopFragment shopFragment;

    String s_minPrice;
    String s_maxPrice;
    int s_orderBy = 4;
    int s_orderType;
    String s_saleType,s_tradeType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeStatusBarTranslucent();
        darkStatusBar();

        setContentView(R.layout.activity_search);
        getParam();
        ButterKnife.bind(this);

        homeService = ServiceManager.getInstance().createService(DDHomeService.class);

        loadHotKeywords();

        screenView.setOnItemClickLisener(new ScreeningView.OnItemClickLisener() {
            @Override
            public void onSort(int orderBy, int orderType) {
                s_orderBy = orderBy;
                s_orderType = orderType;

                requestShop();
            }

            @Override
            public void onFilter(String tradeType, String saleType, String minPrice, String maxPrice) {
                s_minPrice = minPrice;
                s_maxPrice = maxPrice;
                s_tradeType = tradeType;
                s_saleType = saleType;

                requestShop();
            }
        });

        initLayout();
    }

    void initLayout() {

        if (!mKeyword.isEmpty()) {
            mKeywordsLayout.setVisibility(View.GONE);
        } else {
            mKeywordsLayout.setVisibility(View.VISIBLE);
            mKeywordEt.requestFocus();
        }

        mKeywordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() > 0) {
                    mCleanBtn.setVisibility(View.VISIBLE);
                } else {
                    mCleanBtn.setVisibility(View.GONE);
                    loadHotKeywords();
                    mKeywordsLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mKeywordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String keyword = textView.getText().toString();
                    search(keyword);
                    return true;

                }
                return false;
            }
        });

        mKeywordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mInputMethodManager.showSoftInput(mKeywordEt, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    mInputMethodManager.hideSoftInputFromWindow(mKeywordEt.getWindowToken(), 0);
                }
            }
        });

    }

    private void loadHotKeywords() {
        ArrayList<String> keywords = searchWordManager.getAll();
        mHotKeywordsLayout.removeAllViews();
        deleteHistoryButton.setVisibility(keywords.size() > 0 ? View.VISIBLE : View.INVISIBLE);

        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ConvertUtil.dip2px(30));
        int margin = ConvertUtil.dip2px(5);
        layoutParams.setMargins(margin, margin, margin, margin);
        int padding = ConvertUtil.dip2px(15);
        for (final String keyword : keywords) {
            TextView textView = new TextView(SearchActivity.this);
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(12);
            textView.setTextColor(getResources().getColor(R.color.default_text_color));
            textView.setBackgroundResource(R.drawable.bg_keyword);
            textView.setText(keyword);
            textView.setPadding(padding, 0, padding, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            mHotKeywordsLayout.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search(keyword);
                }
            });
        }
    }

    void getParam() {
        Intent intent = getIntent();
        if (!(intent == null || intent.getExtras() == null)) {
            mKeyword = getIntent().getExtras().getString("keyword");
            mKeyword = mKeyword == null ? "" : mKeyword;
        }
        mType = intent.getIntExtra(Config.INTENT_KEY_TYPE_NAME, 0);
    }

    //删除搜索历史按钮
    @OnClick(R.id.deleteHistoryButton)
    protected void onDeleteHistoryPressed() {
        DDDeleteDialog dialog = new DDDeleteDialog(context);
        dialog.setTitle("确定忍心删除历史记录吗");
        dialog.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchWordManager.clear();
                loadHotKeywords();
            }
        });
        dialog.show();
    }

    @OnClick(R.id.cancelBtn)
    protected void cancelSearch() {
        finish();
    }

    @OnClick(R.id.cleanBtn)
    protected void cleanKeyword() {
        mKeywordEt.requestFocus();
        mKeywordEt.setText("");
        mCleanBtn.setVisibility(View.GONE);
    }

    @OnClick(R.id.backBtn)
    protected void onBack() {
        finish();
    }

    public void ctrlMode() {
        responseView.setVisibility(View.VISIBLE);
        mKeywordsLayout.setVisibility(View.GONE);
    }

    private void requestShop() {
        if (shopFragment == null) {
            shopFragment = ShopFragment.newInstance("", "", "", s_minPrice, s_maxPrice, s_orderBy, s_orderType,s_saleType,s_tradeType, mKeyword);
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, shopFragment).commit();
        } else {
            shopFragment.requestShopFill(s_minPrice, s_maxPrice, s_orderBy, s_orderType,s_saleType,s_tradeType, mKeyword);
        }
    }

    protected void search(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            ToastUtil.showToast(context,"请输入关键词");
            return;
        }
        ctrlMode();
        mKeyword = keyword;
        mKeywordEt.setText(mKeyword);

        mCleanBtn.setVisibility(View.VISIBLE);
        mCancelBtn.setVisibility(View.VISIBLE);
        mKeywordEt.clearFocus();
        searchWordManager.addKeyword(keyword);
        requestShop();
    }

}
