package com.example.moviebuzz.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviebuzz.data.SearchRepository;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.data.model.SearchMoviesResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<SearchResult> searchDataResult = new MutableLiveData<>();
    private final MutableLiveData<SearchedMovieData> searchedMoviesLiveData= new MutableLiveData<>();
    private final MutableLiveData<PreviousSearchResult> previousSearchResultData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PreviousSearchResult previousSearchResult = new PreviousSearchResult();
    private final SearchedMovieData searchedMovieData = new SearchedMovieData();

    public static Retrofit getRetrofit()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http:/192.168.43.99:8005/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public void setPreviousSearchResult()
    {
        List<SearchMoviesResponse> searchMoviesResponseList = searchDataResult.getValue().getSearchMoviesResponseList();
        previousSearchResult.addPreviousSearchMoviesResponseList(searchMoviesResponseList);
        previousSearchResultData.setValue(previousSearchResult);
    }

    public void setCurrentResult()
    {
        SearchMoviesResponse searchMoviesResponse = getSearchedMovieData().getValue().getCurrentSearchedMovieData();
        previousSearchResult.addPreviousSearchMoviesResponse(searchMoviesResponse);
        previousSearchResultData.postValue(previousSearchResult);
    }

    public void updateSearchResultBackPress()
    {
        if(!searchedMovieData.getClickedMoviesList().empty())
        {
            SearchMoviesResponse searchMovieResponse = searchedMovieData.getClickedMoviesList().pop();
            if(searchMovieResponse != null)
            {
                searchedMovieData.setCurrentSearchedMovieData(searchMovieResponse);
                searchedMoviesLiveData.setValue(searchedMovieData);
            }
        }
    }

    public void updateSearchResultBackPress1()
    {
        if(previousSearchResultData.getValue() != null && previousSearchResult.getPreviousSearchMoviesResponse().size() !=0)
        {
            searchDataResult.setValue(new SearchResult(null,previousSearchResult.getPreviousSearchMoviesResponseList().pop()));

        }
    }

    public LiveData<SearchResult> getSearchResults()
    {
        return searchDataResult;
    }

    public LiveData<SearchedMovieData> getSearchedMovieData() {
            return searchedMoviesLiveData;
      }

      public void clearPreviousSearchData()
      {
          SearchResult searchResult = new SearchResult(null,new ArrayList<>());
          searchDataResult.setValue(searchResult);
      }

      public void currentSearchedMovieData(SearchMoviesResponse searchMoviesResponse)
      {
          searchedMovieData.addClickedData(searchMoviesResponse);
          searchedMovieData.setCurrentSearchedMovieData(searchMoviesResponse);
          searchedMoviesLiveData.setValue(searchedMovieData);
      }

    public void moviesSearchAPIRequest(String jwtToken,SearchApiEnum searchApiEnum, String country_name, double lat, double lon, String genre, int from)
    {

        Observable<List<SearchMoviesResponse>> observable= getSearchResponse(jwtToken,country_name,lat,lon,searchApiEnum,genre,from);
        addDisposal(observable);
    }

    public void addDisposal(Observable<List<SearchMoviesResponse>> observable)
    {
        disposables.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<SearchMoviesResponse>>(){
                            List<SearchMoviesResponse> searchMoviesResponseList = new ArrayList<>();
                            SearchResult previousApiResult = getSearchResults().getValue();
                            String error;
                            @Override
                            public void onNext(List<SearchMoviesResponse> searchMoviesResponseList) {
                                if(previousApiResult != null)
                                {
                                    //System.out.println(previousApiResult.getSearchMoviesResponseList());
                                    List<SearchMoviesResponse> previousSearchResultList = previousApiResult.getSearchMoviesResponseList();
                                    this.searchMoviesResponseList.addAll(previousSearchResultList);
                                }
                                this.searchMoviesResponseList.addAll(searchMoviesResponseList);
                            }
                            @Override
                            public void onError(@NonNull Throwable e) {
                                this.error = e.toString();
                                //System.out.println(e.toString());
                            }
                            @Override
                            public void onComplete() {
                                searchDataResult.setValue(new SearchResult(null,searchMoviesResponseList));
                            }
                        }));
    }



    static Observable<List<SearchMoviesResponse>> getSearchResponse(String jwtToken,String country_name,double lat, double lon,SearchApiEnum query,String genre, int from)
    {

        Retrofit retrofit = getRetrofit();
        return Observable.defer(() -> {
            SearchRepository searchRepository = retrofit.create(SearchRepository.class);
            Call<List<SearchMoviesResponse>> call = null;
            switch (query) {
                case Popular_Movies_Search:
                    call =  searchRepository.searchMoviesByPopularity(jwtToken,from,10);
                    break;
                case Genre_Movies_Search:
                    call = searchRepository.searchMoviesByGenre(jwtToken,genre,from,10);
                    break;
                case Rated_Movies_Search:
                    call = searchRepository.searchMoviesByRating(jwtToken,from,10);
                    break;
                case Location_Based_Search:
                    call = searchRepository.searchMoviesByLocation(jwtToken,country_name,lat,lon,from,10);
                    break;
                case Country_Based_Search:
                    call = searchRepository.searchMoviesByCountry(jwtToken,country_name,from,10);
                    break;
            }
            List<SearchMoviesResponse> obj = call.execute().body();
            return Observable.just(obj);
        });
    }

    public void actorsOtherMoviesSearch(String jwtToken,String name,int from)
    {
        Observable<List<SearchMoviesResponse>> observable= wildCardSearchonCastWriterDirectors(jwtToken,name,from);
        addDisposal(observable);
    }
    static Observable<List<SearchMoviesResponse>> wildCardSearchonCastWriterDirectors(String jwtToken,String name,int from)
    {
        Retrofit retrofit = getRetrofit();
        return Observable.defer(() -> {
            SearchRepository searchRepository = retrofit.create(SearchRepository.class);
            Call<List<SearchMoviesResponse>> call = searchRepository.searchActorsWriterDirectorsOtherMovies(jwtToken,name,from, 10);
            List<SearchMoviesResponse> obj = call.execute().body();
            return Observable.just(obj);
        });
    }

    public void wildCardSearchApi(String jwtToken,String name,int from)
    {
        Observable<List<SearchMoviesResponse>> observable= wildCardSearchonAllFields(jwtToken,name,from);
        addDisposal(observable);
    }

    static Observable<List<SearchMoviesResponse>> wildCardSearchonAllFields(String jwtToken,
                                                                            String name,int from)
    {
        Retrofit retrofit = getRetrofit();
        return Observable.defer(() -> {
            SearchRepository searchRepository = retrofit.create(SearchRepository.class);
            Call<List<SearchMoviesResponse>> call = searchRepository.searchOnAllData(jwtToken,name,from,10);
            List<SearchMoviesResponse> obj = call.execute().body();
            return Observable.just(obj);
        });
    }
    public void clear()
    {
        this.disposables.clear();
    }
}