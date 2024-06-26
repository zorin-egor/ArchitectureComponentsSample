package com.sample.architecturecomponents.core.domain.usecases

import com.sample.architecturecomponents.core.data.repositories.repositories.RepositoriesRepository
import com.sample.architecturecomponents.core.model.Repository
import com.sample.architecturecomponents.core.network.Dispatcher
import com.sample.architecturecomponents.core.network.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject


class GetRepositoriesByNameUseCase @Inject constructor(
    private val repositoriesRepository: RepositoriesRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {
    companion object {
        private const val LIMIT = 30L
        private const val START_PAGE = 1L
    }

    private val repositories = ArrayList<Repository>()
    private val mutex = Any()
    private var previousName = ""
    private var hasNext = true
    private var page = AtomicLong(START_PAGE)

    operator fun invoke(): Flow<List<Repository>> {
        Timber.d("invoke($hasNext, $previousName) - next")

        synchronized(mutex) {
            if (!hasNext || previousName.isEmpty()) {
                return flowOf(repositories.toList())
            }
        }

        return repositoriesRepository.getRepositoriesByName(name = previousName, page = page.get() + 1, limit = LIMIT)
            .map { new ->
                synchronized(mutex) {
                    page.incrementAndGet()
                    hasNext = new.size >= LIMIT
                    new.forEach { item ->
                        if (repositories.find { it.id == item.id } == null) {
                            repositories.add(item)
                        }
                    }
                    repositories.toList()
                }
            }
            .onCompletion {
                Timber.d("invoke($hasNext, $previousName) - onCompletion")
                delay(500)
            }
            .flowOn(dispatcher)
    }

    operator fun invoke(name: String): Flow<List<Repository>> {
        Timber.d("invoke($name, $previousName) - new")

        synchronized(mutex) {
            if (name == previousName && repositories.isNotEmpty()) {
                return flowOf(repositories.toList())
            }
        }

        return repositoriesRepository.getRepositoriesByName(name = name, page = START_PAGE, limit = LIMIT)
            .onStart {
                Timber.d("invoke($name, $previousName) - onStart")
                delay(500)
            }
            .map {
                synchronized(mutex) {
                    previousName = name
                    page.getAndSet(START_PAGE)
                    hasNext = it.size >= LIMIT
                    repositories.clear()
                    repositories.addAll(it)
                    repositories.toList()
                }
            }
            .flowOn(dispatcher)
    }

}