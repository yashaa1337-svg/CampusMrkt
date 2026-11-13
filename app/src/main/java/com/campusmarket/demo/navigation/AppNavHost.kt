package com.campusmarket.demo.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.campusmarket.demo.data.FavoritesState
import com.campusmarket.demo.data.mock.MockData
import com.campusmarket.demo.ui.components.rememberDemoActions
import com.campusmarket.demo.ui.screens.chat.ChatScreen
import com.campusmarket.demo.ui.screens.chats.ChatsScreen
import com.campusmarket.demo.ui.screens.detail.ListingDetailScreen
import com.campusmarket.demo.ui.screens.home.HomeScreen
import com.campusmarket.demo.ui.screens.onboarding.OnboardingScreen
import com.campusmarket.demo.ui.screens.profile.ProfileScreen
import com.campusmarket.demo.ui.screens.search.SearchScreen
import com.campusmarket.demo.ui.screens.sell.SellScreen

@Composable
fun CampusMarketApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = TopLevelDestination.entries.any { it.route == currentRoute }

    val demo = rememberDemoActions()
    val favorites = remember { FavoritesState(MockData.initialFavorites) }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    currentRoute = currentRoute,
                    unreadChats = MockData.unreadChats,
                    onNavigate = { navController.navigateTopLevel(it.route) },
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = demo.snackbarHostState,
                modifier = if (showBottomBar) Modifier else Modifier.navigationBarsPadding(),
            )
        },
        // Screens handle their own insets; the outer scaffold only reserves the bottom bar.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.ONBOARDING,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onContinue = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    },
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    listings = MockData.listings,
                    campuses = MockData.campuses,
                    isFavorite = favorites::isFavorite,
                    onToggleFavorite = favorites::toggle,
                    onListingClick = { navController.navigate(Routes.listing(it.id)) },
                    onSearchClick = { navController.navigateTopLevel(Routes.SEARCH) },
                    onDemoAction = demo::show,
                )
            }

            composable(Routes.SEARCH) {
                SearchScreen(
                    listings = MockData.listings,
                    recentSearches = MockData.recentSearches,
                    popularSearches = MockData.popularSearches,
                    isFavorite = favorites::isFavorite,
                    onToggleFavorite = favorites::toggle,
                    onListingClick = { navController.navigate(Routes.listing(it.id)) },
                    onDemoAction = demo::show,
                )
            }

            composable(Routes.SELL) {
                SellScreen(
                    meetupSpots = MockData.meetupSpots,
                    onDemoAction = demo::show,
                )
            }

            composable(Routes.CHATS) {
                ChatsScreen(
                    conversations = MockData.conversations,
                    userById = MockData::userById,
                    listingById = MockData::listingById,
                    onConversationClick = { navController.navigate(Routes.chat(it.id)) },
                    onDemoAction = demo::show,
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    user = MockData.me,
                    myListings = MockData.myListings(),
                    favoriteListings = favorites.all.mapNotNull(MockData::listingById),
                    reviews = MockData.reviewsForMe,
                    userById = MockData::userById,
                    isFavorite = favorites::isFavorite,
                    onToggleFavorite = favorites::toggle,
                    onListingClick = { navController.navigate(Routes.listing(it.id)) },
                    onDemoAction = demo::show,
                )
            }

            composable(
                route = Routes.LISTING,
                arguments = listOf(navArgument(Routes.ARG_LISTING_ID) { type = NavType.StringType }),
                enterTransition = pushEnter,
                exitTransition = pushExit,
                popEnterTransition = popEnter,
                popExitTransition = popExit,
            ) { entry ->
                val listing = entry.arguments?.getString(Routes.ARG_LISTING_ID)?.let(MockData::listingById)
                val seller = listing?.let { MockData.userById(it.sellerId) }
                if (listing == null || seller == null) {
                    MissingContent(onBack = { navController.popBackStack() })
                } else {
                    ListingDetailScreen(
                        listing = listing,
                        seller = seller,
                        similar = MockData.similarTo(listing),
                        isFavorite = favorites::isFavorite,
                        onToggleFavorite = favorites::toggle,
                        onBack = { navController.popBackStack() },
                        onListingClick = { navController.navigate(Routes.listing(it.id)) },
                        onMessageSeller = {
                            val conversation = MockData.conversationForListing(listing.id)
                            when {
                                seller.id == MockData.me.id -> demo.show("That's your own listing 🙂")
                                conversation != null -> navController.navigate(Routes.chat(conversation.id))
                                else -> demo.show("Demo: new chats with ${seller.firstName} aren't wired up yet")
                            }
                        },
                        onDemoAction = demo::show,
                    )
                }
            }

            composable(
                route = Routes.CHAT,
                arguments = listOf(navArgument(Routes.ARG_CHAT_ID) { type = NavType.StringType }),
                enterTransition = pushEnter,
                exitTransition = pushExit,
                popEnterTransition = popEnter,
                popExitTransition = popExit,
            ) { entry ->
                val conversation = entry.arguments?.getString(Routes.ARG_CHAT_ID)?.let(MockData::conversationById)
                val otherUser = conversation?.let { MockData.userById(it.otherUserId) }
                val listing = conversation?.let { MockData.listingById(it.listingId) }
                if (conversation == null || otherUser == null || listing == null) {
                    MissingContent(onBack = { navController.popBackStack() })
                } else {
                    ChatScreen(
                        conversation = conversation,
                        otherUser = otherUser,
                        listing = listing,
                        onBack = { navController.popBackStack() },
                        onListingClick = { navController.navigate(Routes.listing(listing.id)) },
                        onDemoAction = demo::show,
                    )
                }
            }
        }
    }
}

/** Switch bottom-bar tabs without piling up the back stack; Home stays the root. */
private fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun MissingContent(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "This item is no longer available.", style = MaterialTheme.typography.titleMedium)
        Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) { Text("Go back") }
    }
}

private val pushEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) + fadeIn(tween(300))
}
private val pushExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    fadeOut(tween(200))
}
private val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    fadeIn(tween(200))
}
private val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) + fadeOut(tween(300))
}
