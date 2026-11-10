package com.example.interestsocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private val Bg = Color(0xFF111318)
private val Panel = Color(0xFF1B1E25)
private val Panel2 = Color(0xFF242832)
private val Accent = Color(0xFF20C6B2)
private val TextMain = Color(0xFFF2F4F6)
private val TextSub = Color(0xFF9299A5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { InterestSocialApp() }
    }
}

private enum class Tab(val title: String) {
    HOME("首页"), CIRCLES("圈子"), NEARBY("身边"), MESSAGES("消息"), ME("我的")
}
private data class Post(val title: String, val author: String, val views: String, val emoji: String, val height: Int)
private data class CircleItem(val name: String, val desc: String, val stats: String, val emoji: String)
private data class Person(val name: String, val meta: String, val distance: String, val sign: String, val emoji: String)
private data class Chat(val name: String, val message: String, val time: String, val unread: Int, val emoji: String)

@Composable
private fun InterestSocialApp() {
    var tab by remember { mutableStateOf(Tab.HOME) }
    var homeFeed by remember { mutableStateOf("兴趣") }
    var mySection by remember { mutableStateOf("资料") }
    var circleCategory by remember { mutableStateOf("推荐") }
    var nearbyMode by remember { mutableStateOf("在线") }
    var selectedCircle by remember { mutableStateOf("全部圈子") }
    var searchOpen by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf<String?>(null) }
    var toast by remember { mutableStateOf("") }
    val snackbar = remember { SnackbarHostState() }

    val posts = remember {
        listOf(
            Post("周末去看了期待很久的展览，分享几张照片", "阿树", "1801", "🎨", 210),
            Post("有没有武汉的朋友一起打羽毛球？", "Zzz", "946", "🏸", 150),
            Post("今天的穿搭记录｜简单舒服就好", "小满", "2308", "👟", 190),
            Post("第一次尝试手冲咖啡，香气太治愈了", "Momo", "1280", "☕", 170),
            Post("城市散步计划：寻找街角的惊喜", "北岛", "760", "🌆", 205),
            Post("读完这本书，想和大家聊聊结尾", "林间", "2190", "📚", 160)
        )
    }
    val circles = remember {
        listOf(
            CircleItem("王者荣耀", "全场最靓的仔都在这里", "3.4w 动态 · 26.1w 成员", "🎮"),
            CircleItem("英雄联盟", "峡谷集合，分享你的高光时刻", "3861 动态 · 7.3w 成员", "⚔️"),
            CircleItem("健身", "通过锻炼遇见更好的自己", "5.7w 动态 · 49.2w 成员", "💪"),
            CircleItem("铲屎官", "猫猫狗狗的日常分享基地", "6360 动态 · 13.3w 成员", "🐱"),
            CircleItem("摄影自拍", "用镜头记录生活的美好", "8.6w 动态 · 50.9w 成员", "📷")
        )
    }
    val people = remember {
        listOf(
            Person("就死手机", "20岁 · 164cm · 52kg", "193.6km", "cpdd 有缘来聊", "🌿"),
            Person("网名", "21岁 · 180cm · 62kg", "615.7km", "看清楚，左边的人", "🌙"),
            Person("哈格喜征", "20岁 · 173cm · 58kg", "269.8km", "喜欢音乐和旅行", "🌸"),
            Person("轻暖暖", "20岁 · 185cm · 63kg", "1067.7km", "希望认识有趣的人", "🪴"),
            Person("Rainy", "22岁 · 179cm · 68kg", "357.7km", "保持好奇，保持热爱", "🌧️")
        )
    }
    val chats = remember {
        listOf(
            Chat("森森...", "看看这个！", "19:31", 2, "🌳"),
            Chat("山河终无意", "武汉 · 18:39", "18:39", 1, "🏞️"),
            Chat("Usuali", "哈哈哈哈", "昨天", 0, "🐼"),
            Chat("就是我打你啊", "好呀，周末见", "11/08", 0, "🦊"),
            Chat("小话唠", "你最近在忙什么？", "07/13", 0, "🐻")
        )
    }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Bg, surface = Panel, primary = Accent, onBackground = TextMain,
        onSurface = TextMain, secondary = TextSub
    )) {
        Scaffold(
            containerColor = Bg,
            snackbarHost = { SnackbarHost(snackbar) },
            bottomBar = { BottomNav(tab) { tab = it; detail = null } }
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding)) {
                when (tab) {
                    Tab.HOME -> HomeScreen(homeFeed, { homeFeed = it }, posts,
                        { detail = it }, { detail = "发布动态" }, { searchOpen = true })
                    Tab.ME -> MeScreen(mySection, { mySection = it }, { detail = it })
                    Tab.MESSAGES -> MessagesScreen(chats, { detail = it })
                    Tab.CIRCLES -> CirclesScreen(circleCategory, { circleCategory = it }, circles,
                        { detail = it }, { selectedCircle = it; toast = "已加入：$it" }, { searchOpen = true })
                    Tab.NEARBY -> NearbyScreen(nearbyMode, { nearbyMode = it }, selectedCircle,
                        { selectedCircle = it }, people, { detail = it }, { toast = "招呼已发送" })
                }
            }
        }
        if (searchOpen) {
            AlertDialog(
                onDismissRequest = { searchOpen = false },
                containerColor = Panel,
                title = { Text("搜索") },
                text = {
                    OutlinedTextField(value = searchText, onValueChange = { searchText = it },
                        placeholder = { Text("搜索兴趣、动态或圈子") }, singleLine = true)
                },
                confirmButton = { TextButton(onClick = { searchOpen = false; detail = "搜索：$searchText" }) { Text("搜索") } },
                dismissButton = { TextButton(onClick = { searchOpen = false }) { Text("取消") } }
            )
        }
        if (detail != null) {
            DetailDialog(detail!!, onClose = { detail = null }, onAction = { toast = it; detail = null })
        }
        if (toast.isNotBlank()) {
            LaunchedEffect(toast) { snackbar.showSnackbar(toast); toast = "" }
        }
    }
}

@Composable
private fun BottomNav(current: Tab, onSelect: (Tab) -> Unit) {
    NavigationBar(containerColor = Bg, contentColor = Accent) {
        val icons = listOf(Icons.Default.Home, Icons.Default.Explore, Icons.Default.Place, Icons.Default.ChatBubble, Icons.Default.Person)
        Tab.values().forEachIndexed { index, item ->
            NavigationBarItem(
                selected = current == item, onClick = { onSelect(item) },
                icon = { Icon(icons[index], null, modifier = Modifier.size(21.dp)) },
                label = { Text(item.title, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Accent, selectedTextColor = Accent,
                    unselectedIconColor = TextSub, unselectedTextColor = TextSub,
                    indicatorColor = Panel2
                )
            )
        }
    }
}

@Composable
private fun TopBar(title: String, left: String = "☰", right: String = "", onLeft: () -> Unit = {}, onRight: () -> Unit = {}) {
    Row(Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(left, color = TextMain, fontSize = 21.sp, modifier = Modifier.clickable { onLeft() })
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Text(title, color = TextMain, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
        Text(right, color = TextMain, fontSize = 20.sp, modifier = Modifier.clickable { onRight() })
    }
}

@Composable
private fun HomeScreen(feed: String, onFeed: (String) -> Unit, posts: List<Post>,
                       onDetail: (String) -> Unit, onPublish: () -> Unit, onSearch: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text("☰", fontSize = 20.sp, modifier = Modifier.clickable { onDetail("侧边菜单") })
            Spacer(Modifier.width(24.dp))
            listOf("关注", "兴趣", "广场").forEach {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 11.dp).clickable { onFeed(it) }) {
                    Text(it, color = if (feed == it) TextMain else TextSub, fontWeight = FontWeight.SemiBold)
                    if (feed == it) Spacer(Modifier.padding(top = 3.dp).width(18.dp).height(3.dp).clip(CircleShape).background(Accent))
                }
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.Search, "搜索", tint = TextMain, modifier = Modifier.clickable { onSearch() })
        }
        Box(Modifier.weight(1f)) {
        LazyColumn(contentPadding = PaddingValues(bottom = 18.dp)) {
            item {
                Box(Modifier.fillMaxWidth().padding(horizontal = 12.dp).height(118.dp)
                    .clip(RoundedCornerShape(12.dp)).background(
                        androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color(0xFF403B78), Color(0xFFB05A9D), Color(0xFF1C8F9B)))
                    ).clickable { onDetail("活动专题") }.padding(16.dp), contentAlignment = Alignment.BottomStart) {
                    Column {
                        Text("全网严选 · 兴趣生活", color = Color.White.copy(alpha = .8f), fontSize = 11.sp)
                        Text("拒绝冷场，发现同频的你", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 14.dp)) {
                    listOf("🎧\n和声精灵", "🎼\n学唱", "🎬\n黑林社", "🎮\n求联盟", "📷\n摄影", "🏃\n运动").forEach {
                        Column(Modifier.width(78.dp).clickable { onDetail(it.substringAfter('\n')) },
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(Modifier.size(54.dp).clip(CircleShape).background(Panel2), contentAlignment = Alignment.Center) {
                                Text(it.substringBefore('\n'), fontSize = 26.sp)
                            }
                            Text(it.substringAfter('\n'), color = TextMain, fontSize = 11.sp, modifier = Modifier.padding(top = 5.dp))
                        }
                    }
                }
            }
            item { Text("为你推荐", color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)) }
            items(posts.chunked(2)) { pair ->
                Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    pair.forEach { post ->
                        Column(Modifier.weight(1f).padding(bottom = 10.dp).clip(RoundedCornerShape(10.dp))
                            .background(Panel).clickable { onDetail(post.title) }) {
                            Box(Modifier.fillMaxWidth().height(post.height.dp).background(
                                androidx.compose.ui.graphics.Brush.linearGradient(listOf(Panel2, Color(0xFF343B48)))
                            ), contentAlignment = Alignment.Center) { Text(post.emoji, fontSize = 46.sp) }
                            Text(post.title, color = TextMain, fontSize = 13.sp, maxLines = 2,
                                overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(horizontal = 9.dp, vertical = 8.dp))
                            Row(Modifier.fillMaxWidth().padding(horizontal = 9.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(post.author, color = TextSub, fontSize = 11.sp)
                                Spacer(Modifier.weight(1f))
                                Text("${post.views} 浏览", color = TextSub, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onPublish,
            containerColor = Accent,
            contentColor = Bg,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) { Icon(Icons.Default.Add, "发布") }
        }
    }
}

@Composable
private fun MeScreen(section: String, onSection: (String) -> Unit, onDetail: (String) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        TopBar("我的", right = "⚙️") { onDetail("设置") }
        Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Avatar("嘉", 72)
            Spacer(Modifier.height(10.dp))
            Text("小嘉没有心", color = TextMain, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("ID: 8827361 · 东京", color = TextSub, fontSize = 12.sp)
            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("关注 128", "粉丝 3.2k", "获赞 8.6k").forEach {
                    Text(it, color = TextMain, fontSize = 13.sp)
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            listOf("资料", "动态", "赞过").forEach {
                Text(it, color = if (section == it) Accent else TextSub, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 20.dp).clickable { onSection(it) })
            }
        }
        Spacer(Modifier.height(12.dp))
        when (section) {
            "资料" -> Column(Modifier.padding(horizontal = 16.dp)) {
                CardRow("📍", "个人信息", "年龄 · 身高 · 城市", onDetail)
                CardRow("🎯", "兴趣标签", "摄影 · 咖啡 · 阅读", onDetail)
                CardRow("👥", "加入的圈子", "5 个圈子", onDetail)
            }
            "动态" -> Text("暂无动态", color = TextSub, modifier = Modifier.padding(32.dp))
            "赞过" -> Text("暂无点赞内容", color = TextSub, modifier = Modifier.padding(32.dp))
        }
    }
}

@Composable
private fun MessagesScreen(chats: List<Chat>, onDetail: (String) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        TopBar("消息")
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("看过你", "回复与@", "获赞", "新粉丝").forEach {
                Surface(shape = RoundedCornerShape(20.dp), color = Panel2, modifier = Modifier.clickable { onDetail(it) }) {
                    Text(it, color = TextMain, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                }
            }
        }
        LazyColumn {
            items(chats) { chat ->
                Row(Modifier.fillMaxWidth().clickable { onDetail(chat.name) }.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Avatar(chat.emoji, 46)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Row {
                            Text(chat.name, color = TextMain, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.weight(1f))
                            Text(chat.time, color = TextSub, fontSize = 11.sp)
                        }
                        Text(chat.message, color = TextSub, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    if (chat.unread > 0) {
                        Spacer(Modifier.width(8.dp))
                        Box(Modifier.size(18.dp).clip(CircleShape).background(Accent), contentAlignment = Alignment.Center) {
                            Text("${chat.unread}", color = Bg, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CirclesScreen(category: String, onCategory: (String) -> Unit, circles: List<CircleItem>,
                          onDetail: (String) -> Unit, onJoin: (String) -> Unit, onSearch: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        TopBar("圈子", right = "🔍") { onSearch() }
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 8.dp)) {
            listOf("推荐", "游戏", "生活", "运动", "兴趣").forEach {
                Text(it, color = if (category == it) Accent else TextSub, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 16.dp).clickable { onCategory(it) })
            }
        }
        LazyColumn(contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)) {
            items(circles) { c ->
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(12.dp)).background(Panel)
                    .clickable { onDetail(c.name) }.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Avatar(c.emoji, 48)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(c.name, color = TextMain, fontWeight = FontWeight.SemiBold)
                        Text(c.desc, color = TextSub, fontSize = 12.sp, maxLines = 1)
                        Text(c.stats, color = TextSub, fontSize = 11.sp)
                    }
                    Button(onClick = { onJoin(c.name) }, colors = ButtonDefaults.buttonColors(containerColor = Accent),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
                        Text("加入", color = Bg, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun NearbyScreen(mode: String, onMode: (String) -> Unit, circle: String, onCircle: (String) -> Unit,
                         people: List<Person>, onDetail: (String) -> Unit, onHello: () -> Unit) {
    var filterOpen by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        TopBar("身边")
        Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            listOf("在线", "附近").forEach {
                Text(it, color = if (mode == it) Accent else TextSub, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 16.dp).clickable { onMode(it) })
            }
            Spacer(Modifier.weight(1f))
            Text(circle, color = TextSub, fontSize = 12.sp, modifier = Modifier.clickable { filterOpen = true })
        }
        if (mode == "附近") {
            Text("📍 定位接入提示：正式版可接入地图与附近用户", color = TextSub, fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp))
        }
        LazyColumn(contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)) {
            items(people) { p ->
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(12.dp)).background(Panel)
                    .clickable { onDetail(p.name) }.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Avatar(p.emoji, 48)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(p.name, color = TextMain, fontWeight = FontWeight.SemiBold)
                        Text(p.meta, color = TextSub, fontSize = 11.sp)
                        Text(p.sign, color = TextSub, fontSize = 12.sp, maxLines = 1)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(p.distance, color = TextSub, fontSize = 11.sp)
                        Spacer(Modifier.height(6.dp))
                        Surface(shape = RoundedCornerShape(16.dp), color = Panel2, modifier = Modifier.clickable { onHello() }) {
                            Text("打招呼", fontSize = 11.sp, color = TextSub, modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp))
                        }
                    }
                }
            }
        }
    }
    if (filterOpen) {
        AlertDialog(onDismissRequest = { filterOpen = false }, containerColor = Panel,
            title = { Text("筛选") },
            text = { Column {
                Text("选择圈子", color = TextSub)
                listOf("全部圈子", "运动", "游戏", "摄影", "生活").forEach {
                    Text(it, color = if (circle == it) Accent else TextMain, modifier = Modifier.fillMaxWidth().clickable { onCircle(it); filterOpen = false }.padding(vertical = 8.dp))
                }
            } },
            confirmButton = { TextButton(onClick = { filterOpen = false }) { Text("完成") } })
    }
}

@Composable
private fun Avatar(label: String, size: Int) {
    Box(Modifier.size(size.dp).clip(CircleShape).background(Panel2), contentAlignment = Alignment.Center) {
        Text(label, fontSize = (size * .42).sp)
    }
}
@Composable
private fun CardRow(emoji: String, title: String, subtitle: String, onClick: (String) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(10.dp)).background(Panel).clickable { onClick(title) }.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Avatar(emoji, 42); Spacer(Modifier.width(10.dp))
        Column { Text(title, color = TextMain); Text(subtitle, color = TextSub, fontSize = 11.sp) }
    }
}
@Composable
private fun DetailDialog(title: String, onClose: () -> Unit, onAction: (String) -> Unit) {
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(Modifier.fillMaxWidth(.9f).clip(RoundedCornerShape(18.dp)).background(Panel).padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, color = TextMain, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = onClose) { Icon(Icons.Default.Close, "关闭", tint = TextSub) }
            }
            Spacer(Modifier.height(14.dp))
            Text("这里是「$title」的演示页面。正式版本可在此接入对应业务数据与服务。", color = TextSub, fontSize = 13.sp)
            Spacer(Modifier.height(18.dp))
            Button(onClick = { onAction("操作已完成（演示）") }, modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Accent)) { Text("确定", color = Bg) }
        }
    }
}
