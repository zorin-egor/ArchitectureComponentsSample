package com.sample.architecturecomponents.core.domain.usecases

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.repositories.RepositoriesRepository
import com.sample.architecturecomponents.core.di.Dispatcher
import com.sample.architecturecomponents.core.di.Dispatchers
import com.sample.architecturecomponents.core.model.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.jetbrains.annotations.TestOnly
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject


class GetRepositoriesByNameUseCase @Inject constructor(
    private val repositoriesRepository: RepositoriesRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher,
) {

    companion object {
        private const val LIMIT = 30L
        private const val START_PAGE = 1L
    }

    @TestOnly
    constructor(
        repositoriesRepository: RepositoriesRepository,
        @Dispatcher(Dispatchers.IO) dispatcher: CoroutineDispatcher,
        limitPerPage: Long = LIMIT
    ) : this(
        repositoriesRepository = repositoriesRepository,
        dispatcher = dispatcher
    ) {
        limit = limitPerPage
    }

    private val repositories = ArrayList<Repository>()
    private val lock = Any()
    private var previousName = ""
    private var hasNext = true
    private var page = AtomicLong(START_PAGE)
    private var limit = LIMIT

    operator fun invoke(name: String = previousName): Flow<Result<List<Repository>>> {
        Timber.d("invoke($name, $previousName)")

        synchronized(lock) {
            when {
                name == previousName && !hasNext -> return flowOf(Result.Success(repositories.toList()))
                name != previousName -> page.set(START_PAGE)
                name.isEmpty() -> return emptyFlow()
            }
        }

        return repositoriesRepository.getRepositoriesByName(name = name, page = page.get(), limit = limit)
            .map { new ->
                if (new is Result.Success) {
                    synchronized(lock) {
                        if (name != previousName) repositories.clear()
                        previousName = name
                        page.incrementAndGet()
                        hasNext = new.data.size >= limit
                        new.data.forEach { item ->
                            if (repositories.find { it.id == item.id } == null) {
                                repositories.add(item)
                            }
                        }
                        Result.Success(repositories.toList())
                    }
                } else {
                    new
                }
            }
            .flowOn(dispatcher)
    }

}