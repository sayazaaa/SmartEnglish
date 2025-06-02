package site.smartenglish.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import site.smartenglish.R
import site.smartenglish.ui.compose.CenterAlignedBackArrowTopAppBar
import site.smartenglish.ui.compose.FavBottomSheets
import site.smartenglish.ui.compose.FavBottomSheetsItemData
import site.smartenglish.ui.compose.HtmlRender
import site.smartenglish.ui.compose.ScrollBar
import site.smartenglish.ui.theme.Orange
import site.smartenglish.ui.theme.White

@Composable
fun ArticleDetailScreen(
    onBackClick: () -> Unit = { /* TODO */ },

    ) {
    val isFav by remember { mutableStateOf(false) }
    val date = "2025-05-31 10:44:36"
    val html = """
<article><div data-component="headline-block" class="sc-3b6b161a-0 fDtfvH"><h1 class="sc-f98b1ad2-0 dfvxux">US to double tariffs on steel and aluminium imports to 50%, Trump says</h1></div><div data-component="byline-block" class="sc-3b6b161a-0 dEGcKf"><div data-testid="byline-new" class="sc-801dd632-0 eSlECZ"><div class="sc-801dd632-1 eeJXtz"><time datetime="2025-05-31T02:44:36.585Z" class="sc-801dd632-2 IvNnh">35 minutes ago</time><div class="sc-801dd632-3 gXqOYA"><div data-testid="popoverWrapper" id="popover-wrapper" class="sc-e1993691-1 hIQXRl"><div data-testid="popoverTrigger" id="popover-trigger"><button type="button" data-testid="socialShareTriggerButton" aria-label="Share" class="sc-42d8a745-2 sc-42d8a745-5 sc-42d8a745-6 bHDbVj ffpPdD htCKMu"><span data-testid="button-icon-wrapper" class="sc-42d8a745-0 jNAQSQ"><svg width="12" height="14" viewBox="0 0 12 14" xmlns="http://www.w3.org/2000/svg" category="social" icon="share" aria-hidden="true" class="sc-9c8c5e05-0 kHfxfq"><path fill-rule="evenodd" clip-rule="evenodd" d="M9.51768 8.96851C8.95518 8.96851 8.44393 9.19851 8.07227 9.57018L4.19727 7.36685C4.24893 7.18768 4.2856 7.00185 4.2856 6.80685C4.2856 6.61102 4.24977 6.42518 4.19893 6.2456L8.06143 4.04935C8.43393 4.42602 8.94977 4.66102 9.51768 4.66102C10.661 4.66102 11.5852 3.73643 11.5852 2.60893C11.5852 1.46518 10.661 0.541016 9.51768 0.541016C8.38977 0.541016 7.4656 1.46518 7.4656 2.60893C7.4656 2.78727 7.49602 2.95768 7.53935 3.12352L3.70727 5.37643C3.33143 4.98477 2.80477 4.73935 2.2181 4.73935C1.09018 4.73935 0.166016 5.6631 0.166016 6.80685C0.166016 7.93476 1.09018 8.85851 2.2181 8.85851C2.8006 8.85851 3.32435 8.61726 3.69977 8.23268L7.54602 10.4943C7.49893 10.6681 7.4656 10.8473 7.4656 11.036C7.4656 12.1639 8.38977 13.0881 9.51768 13.0881C10.661 13.0881 11.5852 12.1639 11.5852 11.036C11.5852 9.89268 10.661 8.96851 9.51768 8.96851"></path></svg></span><span data-testid="button-text" aria-hidden="false" class="sc-42d8a745-1 cwJkjb">Share</span></button></div></div><div data-testid="popoverWrapper" id="popover-wrapper" class="sc-e1993691-1 hIQXRl"><div data-testid="popoverTrigger" id="popover-trigger"><button type="button" data-testid="saveButton" aria-label="Save" class="sc-42d8a745-2 sc-42d8a745-5 sc-42d8a745-6 bcpeIB fxnHdQ fYsCgf"><span data-testid="button-icon-wrapper" class="sc-42d8a745-0 jNAQSQ"><svg width="32" height="32" viewBox="0 0 12 14" category="account" icon="saved-items-outlined" aria-hidden="true" class="sc-9c8c5e05-0 kHfxfq"><path d="M5.99996 9.43351L9.46496 11.0741L9.46496 2.0125L2.53496 2.0125L2.53496 11.0741L5.99996 9.43351ZM0.959961 13.5625L0.959961 0.4375L11.04 0.4375L11.04 13.5625L5.99996 11.1761L0.959961 13.5625Z" fill="currentColor"></path></svg></span><span data-testid="button-text" aria-hidden="false" class="sc-42d8a745-1 cwJkjb">Save</span></button></div></div></div></div><div data-testid="byline-new-contributors" class="sc-801dd632-12 jSIeFi"><div class="sc-801dd632-5 kRoBHa"><div><span class="sc-801dd632-7 lasLGY">Brandon Drenon and Natalie Sherman</span><div class="sc-801dd632-8 hwLppI"><span>BBC News</span></div></div></div></div><div class="sc-801dd632-4 jdpfMh"><div data-testid="popoverWrapper" id="popover-wrapper" class="sc-e1993691-1 hIQXRl"><div data-testid="popoverTrigger" id="popover-trigger"><button type="button" data-testid="socialShareTriggerButton" aria-label="Share" class="sc-42d8a745-2 sc-42d8a745-5 sc-42d8a745-6 bHDbVj ffpPdD htCKMu"><span data-testid="button-icon-wrapper" class="sc-42d8a745-0 jNAQSQ"><svg width="12" height="14" viewBox="0 0 12 14" xmlns="http://www.w3.org/2000/svg" category="social" icon="share" aria-hidden="true" class="sc-9c8c5e05-0 kHfxfq"><path fill-rule="evenodd" clip-rule="evenodd" d="M9.51768 8.96851C8.95518 8.96851 8.44393 9.19851 8.07227 9.57018L4.19727 7.36685C4.24893 7.18768 4.2856 7.00185 4.2856 6.80685C4.2856 6.61102 4.24977 6.42518 4.19893 6.2456L8.06143 4.04935C8.43393 4.42602 8.94977 4.66102 9.51768 4.66102C10.661 4.66102 11.5852 3.73643 11.5852 2.60893C11.5852 1.46518 10.661 0.541016 9.51768 0.541016C8.38977 0.541016 7.4656 1.46518 7.4656 2.60893C7.4656 2.78727 7.49602 2.95768 7.53935 3.12352L3.70727 5.37643C3.33143 4.98477 2.80477 4.73935 2.2181 4.73935C1.09018 4.73935 0.166016 5.6631 0.166016 6.80685C0.166016 7.93476 1.09018 8.85851 2.2181 8.85851C2.8006 8.85851 3.32435 8.61726 3.69977 8.23268L7.54602 10.4943C7.49893 10.6681 7.4656 10.8473 7.4656 11.036C7.4656 12.1639 8.38977 13.0881 9.51768 13.0881C10.661 13.0881 11.5852 12.1639 11.5852 11.036C11.5852 9.89268 10.661 8.96851 9.51768 8.96851"></path></svg></span><span data-testid="button-text" aria-hidden="false" class="sc-42d8a745-1 cwJkjb">Share</span></button></div></div><div data-testid="popoverWrapper" id="popover-wrapper" class="sc-e1993691-1 hIQXRl"><div data-testid="popoverTrigger" id="popover-trigger"><button type="button" data-testid="saveButton" aria-label="Save" class="sc-42d8a745-2 sc-42d8a745-5 sc-42d8a745-6 bcpeIB fxnHdQ fYsCgf"><span data-testid="button-icon-wrapper" class="sc-42d8a745-0 jNAQSQ"><svg width="32" height="32" viewBox="0 0 12 14" category="account" icon="saved-items-outlined" aria-hidden="true" class="sc-9c8c5e05-0 kHfxfq"><path d="M5.99996 9.43351L9.46496 11.0741L9.46496 2.0125L2.53496 2.0125L2.53496 11.0741L5.99996 9.43351ZM0.959961 13.5625L0.959961 0.4375L11.04 0.4375L11.04 13.5625L5.99996 11.1761L0.959961 13.5625Z" fill="currentColor"></path></svg></span><span data-testid="button-text" aria-hidden="false" class="sc-42d8a745-1 cwJkjb">Save</span></button></div></div></div></div></div><div data-component="video-block" data-testid="fabl-video-container" class="sc-3b6b161a-0 hoQmHM"><div class="sc-e65d06a4-0 isyHVt"><div data-testid="" style="visibility: visible;" class="sc-e65d06a4-1 ceJpfs" id="bbcMediaPlayer0"><div style="position:relative;z-index:999;height:100%;width:100%;padding-bottom:0"><smp-toucan-player id="toucan-bbcMediaPlayer0" style="position: absolute; width: 100%; height: 100%; left: 0px; right: 0px; flex: 0 0 auto; --smp-colour-on-base-colour: #000000; --smp-controls-text-colour: #FFFFFF; --smp-live-pulse-colour: #FFFFFF; --smp-controls-fill: #FFFFFF; --smp-colour-on-highlight-colour: #FFFFFF; --smp-colour-toggle-focus-visible: #0071F1; --smp-colour: #0071F1; --smp-base-colour: #FFFFFF; --smp-colour-toggle-border: solid #0071F1; --smp-toggle-slider-button-background-color: #FFFFFF; --smp-toggle-slider-button-background-color-off: #000000; --smp-setting-overlay-line-indicator: #FFFFFF; --smp-overlay-panels-controls-fill: #FFFFFF; --smp-fore-colour-hover: #FFFFFF; --smp-fore-colour-tab: #FFFFFF; --smp-cta-fore-colour-hover: #FFFFFF; --smp-cta-fore-colour-tab: #FFFFFF; --smp-subtitles-size-button-tab-colour: #FFFFFF; --smp-seek-bar-unavailable-colour: #3A3C3E; --smp-seek-bar-available-colour: #B0B2B4; --smp-setting-overlay-outline: #545658; --smp-setting-overlay-outline-hover: #545658;" class="displayCover"></smp-toucan-player></div></div><button data-testid="custom-cta" class="sc-ca512d68-1 gXAOlu"><div data-testid="cta-play-icon" class="sc-ca512d68-0 ioSsYT"><svg viewBox="0 0 82 82"><circle cx="41" cy="41" r="37" fill="#fff"></circle><polygon fill="#202224" points="32,25 32,58 60,42"></polygon></svg></div><div class="sc-ca512d68-4 dkPNuU"><div data-testid="cta-duration" class="sc-ca512d68-2 lcfduy">0:39</div></div></button></div></div><div data-component="caption-block" class="sc-3b6b161a-0 jvncPH"><figcaption class="sc-536eff7b-0 FPsqq">Watch: Trump announces 50% tariff on steel and aluminum</figcaption></div><div data-component="text-block" class="sc-3b6b161a-0 dEGcKf"><p class="sc-9a00e533-0 hxuGS">President Donald Trump has announced the US will double its current tariff rate on steel and aluminium imports from 25% to 50%, starting on Wednesday. </p><p class="sc-9a00e533-0 hxuGS">Speaking at a rally in Pittsburgh, Pennsylvania, Trump said the move would help boost the local steel industry and national supply, while decreasing reliance on China.</p><p class="sc-9a00e533-0 hxuGS">Trump also said that ${'$'}14bn would be invested into the area's steel production through a partnership between US Steel and Japan's Nippon Steel, though he later told reporters he was yet to see or approve the final deal.</p><p class="sc-9a00e533-0 hxuGS">The announcement is the latest turn in Trump's rollercoaster approach to tariffs since re-entering office in January. </p></div><div data-testid="ad-unit" data-component="ad-slot" class="sc-cce5f0ff-0 Bitbe"><div data-testid="dotcom-mid_1" id="dotcom-mid_1" class="dotcom-ad lazy" data-ad-slot="{}"></div></div><div data-component="text-block" class="sc-3b6b161a-0 dEGcKf"><p class="sc-9a00e533-0 hxuGS">"There will be no layoffs and no outsourcing whatsoever, and every US steelworker will soon receive a well deserved ${'$'}5,000 bonus," Trump told the crowd, filled with steelworkers, to raucous applause. </p><p class="sc-9a00e533-0 hxuGS">One of the major concerns from steelworkers about the US-Japan trade deal was how Japan would honour workers' union contract which regulates pay and hiring. </p><p class="sc-9a00e533-0 hxuGS">Trump began his remarks by saying he "saved" US Steel, America's biggest steel manufacturer, located in Pittsburgh, with the 25% steel tariffs he implemented during his first term as president in 2018. </p><p class="sc-9a00e533-0 hxuGS">He touted the increase to 50% as a way to ensure US Steel's survival. </p><p class="sc-9a00e533-0 hxuGS">"At 50%, they can no longer get over the fence," he said. "We are once again going to put Pennsylvania steel into the backbone of America, like never before."</p><p class="sc-9a00e533-0 hxuGS">US steel manufacturing has been declining in recent years, and China, India and Japan have pulled away as the world's top producers. Roughly a quarter of all steel used in the US is imported, and the country's reliance on Mexican and Canadian steel has angered Trump.</p><p class="sc-9a00e533-0 hxuGS">The announcement comes amid <a target="_self" href="https://www.bbc.com/news/articles/c8xgdj9kyero" class="sc-f9178328-0 bGFWdi">a court battle over the legality of some of Trump's global tariffs</a>,<b id="" class="sc-d16436d-0 jpoiOL"> </b>which an appeals court has allowed to continue after the Court of International Trade had ordered the administration to halt the taxes. </p><p class="sc-9a00e533-0 hxuGS">His tariffs on steel and aluminium were untouched by the lawsuit.</p></div><figure><div data-component="image-block" class="sc-3b6b161a-0 dFZIgd"><div data-testid="image" class="sc-d1200759-1 kycbVO"><img sizes="(min-width: 1280px) 50vw, (min-width: 1008px) 66vw, 96vw" srcset="https://ichef.bbci.co.uk/news/240/cpsprodpb/028d/live/9bad59a0-3da6-11f0-90c3-e75a1d5507f9.jpg.webp 240w,https://ichef.bbci.co.uk/news/320/cpsprodpb/028d/live/9bad59a0-3da6-11f0-90c3-e75a1d5507f9.jpg.webp 320w,https://ichef.bbci.co.uk/news/480/cpsprodpb/028d/live/9bad59a0-3da6-11f0-90c3-e75a1d5507f9.jpg.webp 480w,https://ichef.bbci.co.uk/news/640/cpsprodpb/028d/live/9bad59a0-3da6-11f0-90c3-e75a1d5507f9.jpg.webp 640w,https://ichef.bbci.co.uk/news/800/cpsprodpb/028d/live/9bad59a0-3da6-11f0-90c3-e75a1d5507f9.jpg.webp 800w,https://ichef.bbci.co.uk/news/1024/cpsprodpb/028d/live/9bad59a0-3da6-11f0-90c3-e75a1d5507f9.jpg.webp 1024w,https://ichef.bbci.co.uk/news/1536/cpsprodpb/028d/live/9bad59a0-3da6-11f0-90c3-e75a1d5507f9.jpg.webp 1536w" src="https://ichef.bbci.co.uk/news/480/cpsprodpb/028d/live/9bad59a0-3da6-11f0-90c3-e75a1d5507f9.jpg.webp" loading="eager" alt="Getty Images Donald Trump speaking at a rally in Pittsburgh, Pennsylvania" class="sc-d1200759-0 dvfjxj"><span class="sc-d1200759-2 gwFzuU">Getty Images</span></div></div></figure><div data-testid="ad-unit" data-component="ad-slot" class="sc-cce5f0ff-0 Bitbe"><div data-testid="dotcom-mid_2" id="dotcom-mid_2" class="dotcom-ad lazy" data-ad-slot="{}"></div></div><div data-component="text-block" class="sc-3b6b161a-0 dEGcKf"><p class="sc-9a00e533-0 hxuGS">"It is a good day for steelworkers," JoJo Burgess, a member of the local United Steelworkers union who was at Trump's rally, told the BBC. </p><p class="sc-9a00e533-0 hxuGS">Mr Burgess, who is also the city mayor of nearby Washington, Pennsylvania, expressed optimism over the reported details of the partnership with Nippon Steel, saying he hoped it would help breed a new generation of steel workers in the area. </p><p class="sc-9a00e533-0 hxuGS">He recalled "making a lot of money" in the years after Trump instituted steel tariffs in his first term.</p><p class="sc-9a00e533-0 hxuGS">Although Burgess would not label himself a Trump supporter, and says he has only voted for Democratic nominees for president in the last two decades, he said: "I'm never going to disagree with something that's going to level the playing field for American manufacturing." </p><p class="sc-9a00e533-0 hxuGS">But so far, the impacts of Trump's tariffs have largely led to global economic chaos. Global trade and markets have upended, and cracks have formed - or widened - among relations between the US and other countries, including some of its closest partners. </p><p class="sc-9a00e533-0 hxuGS">The levies have worsened relations between China and the US, the world's two biggest global economies, and launched the countries into a tit-for-tat trade battle.</p></div><div data-component="text-block" class="sc-3b6b161a-0 dEGcKf"><ul class="sc-6f869981-0 fusmZA"><li class="sc-734a601e-0 huNRXw"><a target="_self" href="https://www.bbc.com/news/articles/cvg788jrxrro" class="sc-f9178328-0 jsleNP">ANALYSIS: Tariffs court fight threatens Trump's power to wield his favourite economic weapon</a></li><li class="sc-734a601e-0 huNRXw"><a target="_self" href="https://www.bbc.com/news/articles/c8xgdj9kyero" class="sc-f9178328-0 jsleNP">EXPLAINER: Trump tariffs get to stay in place for now. What happens next?</a></li><li class="sc-734a601e-0 huNRXw"><a target="_self" href="https://www.bbc.com/news/articles/cje7zex3njwo" class="sc-f9178328-0 jsleNP">China hits back after Trump claims it is 'violating' tariff truce</a></li></ul></div><div data-testid="ad-unit" data-component="ad-slot" class="sc-cce5f0ff-0 Bitbe"><div data-testid="dotcom-mid_3" id="dotcom-mid_3" class="dotcom-ad lazy" data-ad-slot="{}"></div></div><div data-component="text-block" class="sc-3b6b161a-0 dEGcKf"><p class="sc-9a00e533-0 hxuGS">On Friday, without providing details, <a target="_self" href="https://www.bbc.com/news/articles/cje7zex3njwo" class="sc-f9178328-0 bGFWdi">President Trump accused China of violating a truce</a> they'd reached over tariffs earlier this month over talks in Geneva.</p><p class="sc-9a00e533-0 hxuGS">US Trade Representative Jamieson Greer later clarified that China had not been removing non-tariff barriers as agreed under the deal.</p><p class="sc-9a00e533-0 hxuGS">China then shot back with its own accusations of US wrongdoing. Beijing's response on Friday did not address the US claims directly but urged the US to "cease discriminatory restrictions against China".</p><p class="sc-9a00e533-0 hxuGS">China is the world's largest manufacturer of steel, responsible for over half of global steel production, according to World Steel Association statistics from 2022.</p><p class="sc-9a00e533-0 hxuGS">"If you don't have steel, you don't have a country. You don't have a country, you can't make a military. What are we going to do? Say, 'Let's go to China to get our steel from the army tanks,'" Trump quipped at the Pittsburgh rally on Friday.</p></div><div data-component="embed-block" class="sc-3b6b161a-0 dHQIrb"><div><div class="sc-3b6b161a-0 kDhnfC"><div data-testid="include-with-script" class="sc-afbb24a9-0 fJOqOa"><iframe src="https://flo.uri.sh/visualisation/23502113/embed?auto=1" frameborder="0" scrolling="no" height="692" width="700" style="width:100%;" title="Interactive or visual content"></iframe></div></div></div></div><div data-testid="ad-unit" data-component="ad-slot" class="sc-cce5f0ff-0 Bitbe"><div data-testid="dotcom-mid_4" id="dotcom-mid_4" class="dotcom-ad lazy" data-ad-slot="{}"></div></div><div data-component="text-block" class="sc-3b6b161a-0 dEGcKf"><p class="sc-9a00e533-0 hxuGS">Trump's roughly hour-long, wide-ranging rally speech hinted at the deal he said he made with Japan's Nippon Steel but did not offer any new details. Both companies have not yet confirmed any deal was completed.</p><p class="sc-9a00e533-0 hxuGS">While campaigning for president, Trump had said he would block foreign acquisition of US Steel, the storied 124-year-old American steel company. It's unclear how the reported partnership would operate and who would own the company. </p><p class="sc-9a00e533-0 hxuGS">White House officials said Trump had convinced Japan's Nippon Steel to boost its investment in the US and give the government key say over the operations of the US factories. </p><p class="sc-9a00e533-0 hxuGS">According to US media, Japan plans to invest ${'$'}14bn over 14 months. </p><p class="sc-9a00e533-0 hxuGS">Other reported details include that the companies had said they would maintain ownership of US steel in the US, with US citizens on the board and in leadership positions; pledged not to cut production for 10 years; and agreed to give the government the right to veto potential production cuts after that period.</p></div><div data-component="links-block" class="sc-3b6b161a-0 dHQIrb"><div class="sc-666b6d83-0 sc-c4fb5674-0 gMiqnK iuHqSH"><div data-testid="links-grid" class="sc-666b6d83-0 sc-c4fb5674-1 kIEICy bPCWIP"><div data-testid="chester-card" data-indexcard="true" class="sc-225578b-0 btdqbl"><div data-testid="anchor-inner-wrapper"><a href="https://www.bbc.com/news/articles/clynkqw236po" data-testid="external-anchor" target="_self" class="sc-8a623a54-0 hMvGwj"><div data-testid="chester-article" class="sc-bec5e5d2-0 eUcHmo"><div class="sc-9d830f2a-1 dOyfhs"><div class="sc-9d830f2a-0 eKWlJZ"><h2 data-testid="card-headline" class="sc-9d830f2a-3 fWzToZ">US green energy firms brace for federal funding cuts</h2></div></div></div></a></div></div></div></div></div><div data-component="tags" class="sc-3b6b161a-0 dHQIrb"><div class="sc-6bf80075-0 kkfrXx"><div data-testid="anchor-inner-wrapper"><a href="/news/topics/c9vwxgl4p0dt" data-testid="internal-link" class="sc-57521d02-0 emeJAW">Trump tariffs</a></div><div data-testid="anchor-inner-wrapper"><a href="/news/topics/cg5rv39y9mmt" data-testid="internal-link" class="sc-57521d02-0 emeJAW">Global trade</a></div><div data-testid="anchor-inner-wrapper"><a href="/news/topics/cp7r8vgl2lgt" data-testid="internal-link" class="sc-57521d02-0 emeJAW">Donald Trump</a></div><div data-testid="anchor-inner-wrapper"><a href="/news/topics/cwnpxwzd269t" data-testid="internal-link" class="sc-57521d02-0 emeJAW">US politics</a></div><div data-testid="anchor-inner-wrapper"><a href="/news/topics/cx1m7zg01xyt" data-testid="internal-link" class="sc-57521d02-0 emeJAW">United States</a></div></div></div></article>
                """.trimIndent()
    var isFavShow by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedBackArrowTopAppBar(
                title = "", onBackClick = onBackClick, actions = {
                    IconButton(
                        onClick = {
                            // 打开底部弹窗
                            isFavShow = true

                        }, modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isFav) R.drawable.kid_star_fill else R.drawable.kid_star
                            ),
                            contentDescription = "收藏",
                            tint = if (isFav) Orange else White,
                        )
                    }
                })
        },
    ) { inn ->

        // 获取父容器高度
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(inn)
        ) {
            val density = LocalDensity.current
            val parentHeight = maxHeight
            val scrollState = rememberScrollState()
            val scope = rememberCoroutineScope() // 添加协程作用域

            var contentHeight by remember { mutableStateOf(0) }
            val viewportPx = with(density) { parentHeight.roundToPx() }

            if (isFavShow) {
                FavBottomSheets(
                    title = "将文章收藏至",
                    onDismiss = { isFavShow = false },
                    onAddToFav = {
                        //TODO 添加到收藏夹
                    },
                    onCreateFav = {
                        //TODO 新建收藏夹
                    },
                    error = painterResource(R.drawable.news),
                    items =
                        //TODO: 替换成实际的收藏夹数据
                        listOf(
                            FavBottomSheetsItemData(
                                id = 1,
                                title = "我的收藏",
                                cover = ""
                            ),
                            FavBottomSheetsItemData(
                                id = 2,
                                title = "新建收藏夹",
                                cover = ""
                            )
                        )

                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 27.dp, end = 27.dp, top = 27.dp)
                    .verticalScroll(scrollState)
                    .onSizeChanged { size ->
                        contentHeight = size.height
                    }) {
                // 内容部分
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(17.dp)
                            .background(Orange)
                    )
                    Spacer(Modifier.width(11.dp))
                    Text(
                        text = date,
                        color = White,
                        modifier = Modifier.padding(top = 2.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.times))
                    )
                }
                Spacer(Modifier.height(20.dp))
                HtmlRender(html = html)
            }

            // 滚动条
            ScrollBar(scrollPosition = {
                if (scrollState.maxValue > 0) scrollState.value.toFloat() / scrollState.maxValue
                else 0f
            }, scrollHeight = {
                if (contentHeight > viewportPx) viewportPx.toFloat() / contentHeight
                else 0f
            }, onScrollPositionChange = { newPosition ->
                val newScrollValue = (newPosition * scrollState.maxValue).toInt()
                scope.launch {
                    scrollState.scrollTo(newScrollValue)
                }
            })
        }
    }
}


