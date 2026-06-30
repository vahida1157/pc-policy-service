package com.vahak.pc.policy.domain

import jakarta.persistence.*

@Entity
@Table(name = "browser_settings")
class BrowserSettings(
    @Id
    val childId: String,
    val searchEngine: String,
    val isCartoonWorldEnabled: Boolean,
    val filterMode: String,
    val updatedAt: Long
)

@Entity
@Table(name = "browser_allowed_sites", uniqueConstraints = [UniqueConstraint(columnNames = ["childId", "url"])])
class BrowserAllowedSite(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val childId: String,
    val url: String,
    val label: String,
    var isActive: Boolean,
    var updatedAt: Long
)

@Entity
@Table(name = "browser_blocked_sites", uniqueConstraints = [UniqueConstraint(columnNames = ["childId", "url"])])
class BrowserBlockedSite(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val childId: String,
    val url: String,
    var isActive: Boolean,
    var updatedAt: Long
)

@Entity
@Table(name = "browser_blocked_keywords", uniqueConstraints = [UniqueConstraint(columnNames = ["childId", "keyword"])])
class BrowserBlockedKeyword(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val childId: String,
    val keyword: String,
    var isActive: Boolean,
    var updatedAt: Long
)