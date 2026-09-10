package com.example.jetmovie.ui.detail;

import com.example.jetmovie.movie_detail.domain.repository.MovieDetailRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class DetailViewModel_Factory implements Factory<DetailViewModel> {
  private final Provider<MovieDetailRepository> repositoryProvider;

  public DetailViewModel_Factory(Provider<MovieDetailRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DetailViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static DetailViewModel_Factory create(Provider<MovieDetailRepository> repositoryProvider) {
    return new DetailViewModel_Factory(repositoryProvider);
  }

  public static DetailViewModel newInstance(MovieDetailRepository repository) {
    return new DetailViewModel(repository);
  }
}
