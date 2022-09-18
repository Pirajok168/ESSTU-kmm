package ru.esstu.android.student.news.selector_screen.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.soywiz.klock.DateTime
import kotlinx.coroutines.launch
import ru.esstu.domain.utill.helpers.ScheduleSource
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.news.entities.Attachment
import ru.esstu.student.news.entities.NewsNode


data class SelectorScreenState(
    val title: String = "",
    val node: NewsNode? = null,

    val today: DateTime? = null,
    //val closestLesson: Pair<LessonPeriod, List<StudentNode>>? = null,

   // val anchor: ParityAnchor? = null,
    val isAnchorLoading: Boolean = false,
    val anchorError: ResponseError? = null,

  //  val schedule: Map<LessonPeriod, List<StudentNode>> = emptyMap(),
    val isScheduleLoading: Boolean = false,
    val isScheduleLoaded: Boolean = false,
    val scheduleError: ResponseError? = null,

    val preferredSource: ScheduleSource = ScheduleSource.Api,
   // val lessonsDefaults: StudyPeriodDefaults = StudyPeriodDefaults()
) {
   // val todayWeek get() = if (today != null) anchor?.getTodayWeek(today) else null
}

sealed class SelectorScreenEvents {
    data class PassNode(val node: NewsNode, val title: String) : SelectorScreenEvents()
    //data class PassTodayTime(val time: LocalDateTime) : SelectorScreenEvents()
    object LoadAnchor : SelectorScreenEvents()
    object LoadSchedule : SelectorScreenEvents()
    data class DownloadAttachment(val attachment: Attachment) : SelectorScreenEvents()
    data class UpdateAttachment(val attachment: Attachment) : SelectorScreenEvents()
}


class SelectorViewModel  constructor(
   // private val scheduleRepo: IScheduleRepository = EngineSDK.scheduleModule.repo,
    //private val downloadRepository: IFileDownloadRepository,
) : ViewModel() {
    var state by mutableStateOf(SelectorScreenState())
        private set


    fun onEvent(event: SelectorScreenEvents) {
        when (event) {
            is SelectorScreenEvents.PassNode -> state = state.copy(title = event.title, node = event.node)
      //      is SelectorScreenEvents.DownloadAttachment -> viewModelScope.launch { downloadRepository.downloadFile(event.attachment) }
            is SelectorScreenEvents.UpdateAttachment -> onUpdateAttachment(event.attachment)

           /* is SelectorScreenEvents.PassTodayTime -> {
                state = state.copy(today = event.time); setClosestLesson()
            }*/
            SelectorScreenEvents.LoadAnchor -> viewModelScope.launch { onLoadAnchor() }
            SelectorScreenEvents.LoadSchedule -> viewModelScope.launch { onLoadSchedule() }
            else -> {}
        }
    }

    private fun setClosestLesson() {
       /* val localTime = state.today?.toLocalTime() ?: return
        val scList = state.schedule.toList()
        val closestLesson = scList.firstOrNull { it.first.inIt(localTime) } ?: scList.firstOrNull { it.first.startTime > localTime }
        state = state.copy(closestLesson = closestLesson)*/
    }

    private fun onUpdateAttachment(attachment: Attachment) {
        val node = state.node ?: return

        state = state.copy(
            node = node.copy(
                attachments = node.attachments.map { att ->
                    if (att.id != attachment.id) return@map att
                    attachment
                }
            )
        )
    }

    private suspend fun onLoadAnchor() {

        /*scheduleRepo.getAnchor().collect { anchor ->
            state = when (anchor) {
                is FlowResponse.Error -> state.copy(anchorError = anchor.error)
                is FlowResponse.Loading -> state.copy(isAnchorLoading = anchor.isLoading)
                is FlowResponse.Success -> state.copy(anchor = anchor.data, anchorError = null)
            }
        }*/
    }

    //private val studyPeriods = StudyPeriodDefaults().toList()

    private suspend fun onLoadSchedule() {
      /*  val anchor = state.anchor ?: return
        val today = state.today?.toLocalDate() ?: return
        val week = anchor.getTodayWeek(today)
        val weekDay = DayOfWeek.values().getOrNull(today.dayOfWeek - 1) ?: return
        scheduleRepo.getSchedule(
            preferredSource = state.preferredSource,
            week = week,
            weekDay = weekDay
        ).collect { schedule ->
            when (schedule) {
                is FlowResponse.Error -> state = state.copy(scheduleError = schedule.error)
                is FlowResponse.Loading -> state = state.copy(isScheduleLoading = schedule.isLoading)
                is FlowResponse.Success -> {

                    //TODO убрать костыль .first().nodes. добавить отображение нескольких расписаний в виде селектора
                    val timedSchedule = schedule.data.firstOrNull()?.nodes?.groupBy { studyPeriods.getOrNull(it.lessonOrder.ordinal) }
                        ?.mapNotNull {
                            val order = it.key ?: return@mapNotNull null
                            return@mapNotNull order to it.value
                        }?.toMap()

                    when (timedSchedule) {
                        null -> {
                            state = state.copy(isScheduleLoaded = true, anchorError = null)
                        }
                        else -> {
                            state = state.copy(schedule = timedSchedule, isScheduleLoaded = true, anchorError = null)
                            setClosestLesson()
                        }
                    }
                }
            }
        }*/
    }


}