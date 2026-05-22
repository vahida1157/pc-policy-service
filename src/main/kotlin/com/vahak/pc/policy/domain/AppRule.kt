package com.vahak.pc.policy.domain

import jakarta.persistence.*
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(
    name = "app_rules",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["child_id", "package_name"]) // Prevents duplicates!
    ]
)
class AppRule(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID? = null,

    @Column(name = "child_id", nullable = false)
    val childId: UUID,

    @Column(name = "package_name", nullable = false)
    val packageName: String,

    @Column(name = "is_allowed", nullable = false)
    var isAllowed: Boolean = false,

    @UpdateTimestamp
    var updatedAt: Instant? = null
)