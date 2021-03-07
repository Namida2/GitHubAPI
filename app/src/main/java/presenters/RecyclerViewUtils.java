package presenters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapi.Anim;
import com.example.testapi.adapters.UsersRecyclerViewAdapter;

import model.GitHttpRequest;
import model.GitUsers;

import static com.example.testapi.MainActivity.floatingActionButton;
import static com.example.testapi.MainActivity.gitHttpRequest;


public class RecyclerViewUtils {
    public static void prepareRecyclerView(RecyclerView recyclerView, UsersRecyclerViewAdapter adapter) {
        recyclerView.setRecyclerListener(holder -> {
            if(holder.getItemViewType() == 0 && adapter.getUsersList().size() > 0) {
                Anim.Companion.showView(floatingActionButton);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Runnable waitForGitHttpRequest = () -> {
                    synchronized (gitHttpRequest) {
                        try {
                            gitHttpRequest.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        gitHttpRequest = new GitHttpRequest(recyclerView.getContext(), NetworkUtils.getURL(EditTextUtils.getCurrentText()));
                        gitHttpRequest.start();
                    }
                };
                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (GitUsers.getUsersList().size() > 0 && gitHttpRequest.isAlive()) {
                        Thread thread = new Thread(waitForGitHttpRequest);
                        thread.start();
                    }
                    else if (GitUsers.getUsersList().size() > 0) {
                        gitHttpRequest = new GitHttpRequest(recyclerView.getContext(), NetworkUtils.getURL(EditTextUtils.getCurrentText()));
                        gitHttpRequest.start();
                    }
                }
                if (!recyclerView.canScrollVertically(-1)) {
                    if(floatingActionButton.getAlpha() == 1) {
                        Anim.Companion.hideView(floatingActionButton);
                    }
                }
            }
        });
    }
}
