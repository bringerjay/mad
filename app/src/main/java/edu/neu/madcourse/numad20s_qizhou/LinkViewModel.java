package edu.neu.madcourse.numad20s_qizhou;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LinkViewModel extends AndroidViewModel {

    private LinkRepository linkRepository;

    private LiveData<List<Link>> allLinks;

    public LinkViewModel (Application application) {
        super(application);
        linkRepository = new LinkRepository(application);
        allLinks = linkRepository.getAllLinks();
    }

    LiveData<List<Link>> getAllLinks() { return allLinks; }

    public void insert(Link link) { linkRepository.insert(link); }
}
