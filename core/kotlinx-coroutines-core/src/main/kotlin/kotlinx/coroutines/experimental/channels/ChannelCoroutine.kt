/*
 * Copyright 2016-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kotlinx.coroutines.experimental.channels

import kotlinx.coroutines.experimental.AbstractCoroutine
import kotlinx.coroutines.experimental.handleCoroutineException
import kotlin.coroutines.experimental.CoroutineContext

internal open class ChannelCoroutine<E>(
    parentContext: CoroutineContext,
    private val _channel: Channel<E>,
    active: Boolean
) : AbstractCoroutine<Unit>(parentContext, active), Channel<E> by _channel {
    val channel: Channel<E>
        get() = this

    override fun onCancellation(exceptionally: CompletedExceptionally?) {
        val cause = exceptionally?.cause
        if (!_channel.cancel(cause) && cause != null)
            handleCoroutineException(context, cause)
    }

    override fun cancel(cause: Throwable?): Boolean = super.cancel(cause)
}
