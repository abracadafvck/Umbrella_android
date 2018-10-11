package org.secfirst.umbrella.whitelabel.data.database.lesson

import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import org.secfirst.umbrella.whitelabel.data.database.AppDatabase
import org.secfirst.umbrella.whitelabel.data.database.BaseModel
import org.secfirst.umbrella.whitelabel.data.database.content.Module
import org.secfirst.umbrella.whitelabel.data.database.content.Subject
import org.secfirst.umbrella.whitelabel.data.database.difficulty.Difficulty

data class Lesson(var moduleId: Long,
                  var moduleTitle: String = "",
                  var pathIcon: String = "",
                  var topics: List<Subject> = listOf()) : ExpandableGroup<Subject>(moduleTitle, topics) {
}

@Table(database = AppDatabase::class)
data class TopicPreferred(@PrimaryKey(autoincrement = true)
                          var id: Long = 0,
                          @ForeignKey
                          var difficulty: Difficulty? = null) : BaseModel() {
    constructor(difficulty: Difficulty?) : this(0, difficulty)
}

fun List<Module>.toLesson(): List<Lesson> {
    val lessons = mutableListOf<Lesson>()
    val moduleSorted = this.sortedWith(compareBy { it.index })
    moduleSorted.forEach { module ->
        val subjectSorted = module.subjects.sortedWith(compareBy { it.index })
        module.subjects = subjectSorted.toMutableList()
        val lesson = Lesson(module.id, module.title, module.resourcePath, module.subjects)
        lessons.add(lesson)
    }
    return lessons
}