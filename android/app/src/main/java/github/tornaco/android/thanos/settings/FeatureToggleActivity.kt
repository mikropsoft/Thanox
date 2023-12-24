/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package github.tornaco.android.thanos.settings

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.main.PrebuiltFeatures
import github.tornaco.android.thanos.module.compose.common.ComposeThemeActivity
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ListItem
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold
import github.tornaco.android.thanos.pref.AppPreference

class FeatureToggleActivity : ComposeThemeActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, FeatureToggleActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Composable
    override fun Content() {
        ThanoxSmallAppBarScaffold(
            title = {
                androidx.compose.material3.Text(
                    text = stringResource(id = R.string.pref_title_feature_toggle),
                    style = TypographyDefaults.appBarTitleTextStyle()
                )
            },
            onBackPressed = {
                thisActivity().finish()
            },
            actions = {

            }
        ) { paddings ->
            val context = LocalContext.current
            val thanox = remember {
                ThanosManager.from(context)
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = paddings
            ) {
                items(PrebuiltFeatures.all {
                    (it.requiredFeature == null || thanox.hasFeature(it.requiredFeature))
                }.flatMap { it.items }) { feature ->
                    var isEnabled by remember {
                        mutableStateOf(AppPreference.isAppFeatureEnabled(context, feature.id))
                    }
                    ListItem(title = stringResource(id = feature.titleRes),
                        checked = isEnabled,
                        onCheckedChange = {
                            AppPreference.setAppFeatureEnabled(context, feature.id, it)
                            isEnabled = AppPreference.isAppFeatureEnabled(context, feature.id)
                        },
                        onClick = {})
                }
            }
        }
    }
}