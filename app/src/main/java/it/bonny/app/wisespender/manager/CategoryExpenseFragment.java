package it.bonny.app.wisespender.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.CategoryListAdapter;
import it.bonny.app.wisespender.util.RecyclerViewClickAccountInterface;
import it.bonny.app.wisespender.util.RecyclerViewClickCategoryInterface;

public class CategoryExpenseFragment extends Fragment implements RecyclerViewClickCategoryInterface {
    private View root;
    private List<CategoryBean> categoryBeanList = new ArrayList<>();
    private RecyclerView listViewCategory;
    private DatabaseHelper db;
    private int pos = -1;
    private CategoryListAdapter categoryAdapter;
    private MaterialButton buttonNewCategory;
    private ImageView imageView;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_category_expense, container, false);
        init();

        categoryAdapter = new CategoryListAdapter(categoryBeanList, getActivity(), this);
        listViewCategory.setHasFixedSize(true);
        listViewCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        listViewCategory.setAdapter(categoryAdapter);

        callDB();

        buttonNewCategory.setOnClickListener(view -> {
            Intent intent = new Intent(root.getContext(), NewEditCategoryActivity.class);
            CategoryBean categoryBeanNew = new CategoryBean(TypeObjectBean.CATEGORY_EXPENSE);
            intent.putExtra("categoryBean", categoryBeanNew);
            openSomeActivityForResult(intent);
        });

        return root;
    }

    private void init() {
        db = new DatabaseHelper(root.getContext());
        listViewCategory = root.findViewById(R.id.listViewCategory);
        buttonNewCategory = root.findViewById(R.id.buttonNewCategory);
        imageView = root.findViewById(R.id.listEmpty);
        progressBar = root.findViewById(R.id.progressBar);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void callDB() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        listViewCategory.setVisibility(View.GONE);
        service.execute(() -> {
            categoryBeanList = db.getAllCategoryBeansToTypeCategory(TypeObjectBean.CATEGORY_EXPENSE);

            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    categoryAdapter.insertCategoryBeanList(categoryBeanList);
                    elementSetVisibility();
                });
            }else {
                elementSetVisibility();
            }
        });
    }

    private void elementSetVisibility() {
        if(categoryBeanList != null && categoryBeanList.size() > 0) {
            listViewCategory.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setVisibility(View.VISIBLE);
            listViewCategory.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(int position) {
        pos = position;
        Intent intent = new Intent(root.getContext(), NewEditCategoryActivity.class);
        intent.putExtra("categoryBean", categoryAdapter.findCategoryBean(pos));
        openSomeActivityForResult(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data != null) {
                    int typeList = data.getIntExtra("typeAction", -1);
                    CategoryBean categoryBean = data.getParcelableExtra("categoryBean");
                    if(typeList == TypeObjectBean.RETURN_DELETE) {
                        categoryAdapter.deleteCategoryBean(pos);
                    }else if(typeList == TypeObjectBean.RETURN_EDIT) {
                        categoryAdapter.updateCategoryBean(categoryBean, pos);
                    }else if(typeList == TypeObjectBean.RETURN_NEW) {
                        categoryAdapter.insertCategoryBean(categoryBean);
                    }
                }
            }
        }
    });

    public void openSomeActivityForResult(Intent intent) {
        someActivityResultLauncher.launch(intent);
    }

}