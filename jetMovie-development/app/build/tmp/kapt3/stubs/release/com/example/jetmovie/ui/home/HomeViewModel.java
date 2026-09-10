package com.example.jetmovie.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0011\u001a\u00020\u000eH\u0002J\b\u0010\u0012\u001a\u00020\u000eH\u0002J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\f\u00a8\u0006\u0017"}, d2 = {"Lcom/example/jetmovie/ui/home/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/example/jetmovie/movie/domain/repository/MovieRepository;", "(Lcom/example/jetmovie/movie/domain/repository/MovieRepository;)V", "_homeState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/jetmovie/ui/home/HomeState;", "_searchState", "homeState", "Lkotlinx/coroutines/flow/StateFlow;", "getHomeState", "()Lkotlinx/coroutines/flow/StateFlow;", "searchJob", "Lkotlinx/coroutines/Job;", "searchState", "getSearchState", "fetchDiscoverMovie", "fetchTrendingMovie", "searchMoviesDebounced", "", "query", "", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.example.jetmovie.movie.domain.repository.MovieRepository repository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.jetmovie.ui.home.HomeState> _homeState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.example.jetmovie.ui.home.HomeState> homeState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.jetmovie.ui.home.HomeState> _searchState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.example.jetmovie.ui.home.HomeState> searchState = null;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job searchJob;
    
    @javax.inject.Inject
    public HomeViewModel(@org.jetbrains.annotations.NotNull
    com.example.jetmovie.movie.domain.repository.MovieRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.example.jetmovie.ui.home.HomeState> getHomeState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.example.jetmovie.ui.home.HomeState> getSearchState() {
        return null;
    }
    
    private final kotlinx.coroutines.Job fetchDiscoverMovie() {
        return null;
    }
    
    public final void searchMoviesDebounced(@org.jetbrains.annotations.NotNull
    java.lang.String query) {
    }
    
    private final kotlinx.coroutines.Job fetchTrendingMovie() {
        return null;
    }
}