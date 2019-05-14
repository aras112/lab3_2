package edu.iis.mto.staticmock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.reader.NewsReader;


import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class, PublishableNews.class})
public class NewsLoaderTest
    {

    //behavior
    @Mock
    ConfigurationLoader configurationLoader;
    @Mock
    NewsReader newsReader;
    @Mock
    PublishableNews publishableNews;

    //data struct

    Configuration configuration = new Configuration();
    IncomingNews incomingNews = new IncomingNews();
    NewsLoader newsLoader = new NewsLoader();


    @Before
    public void setUp() throws Exception
        {
        prepareStaticClass();
        setDefaultValue();
        }

    @Test
    public void givenOneATypeSubscription_whenLoaderLoadsNews_thenAddForSubscriptionShouldOneRun()
        {
        //given
        addSomeNewsToLoader(SubsciptionType.A);

        //when
        loaderLoadsNews();

        //then
        verify(publishableNews, times(1)).addForSubscription(any(String.class), any());
        }

    @Test
    public void givenOneBTypeSubscription_whenLoaderLoadsNewsTwoTimes_thenAddForSubscriptionShouldTwoTimesRun()
        {
        //given
        addSomeNewsToLoader(SubsciptionType.B);

        //when
        loaderLoadsNews();
        loaderLoadsNews();

        //then
        verify(publishableNews, times(2)).addForSubscription(any(String.class), any());

        }

    @Test
    public void givenOneCTypeSubscription_whenLoaderLoadsNewsTwoTimes_thenAddForSubscriptionShouldTwoTimesRun()
        {
        //given
        addSomeNewsToLoader(SubsciptionType.C);

        //when
        loaderLoadsNews();
        loaderLoadsNews();

        //then
        verify(publishableNews, times(2)).addForSubscription(any(String.class), any());
        }

    @Test
    public void givenFourCTypeSubscription_whenLoaderLoads_thenAddForSubscriptionShouldFourTimesRun()
        {
        //given
        addSomeNewsToLoader(SubsciptionType.C);
        addSomeNewsToLoader(SubsciptionType.C);
        addSomeNewsToLoader(SubsciptionType.C);
        addSomeNewsToLoader(SubsciptionType.C);

        //when
        loaderLoadsNews();

        //then
        verify(publishableNews, times(4)).addForSubscription(any(String.class), any());
        }

    @Test
    public void givenOneNotSubscriptionNews_whenLoaderLoadsNews_thenAddPublicInfoShouldOneTimesRun()
        {
        //given
        addSomeNewsToLoader(SubsciptionType.NONE);

        //when
        loaderLoadsNews();

        //then
        verify(publishableNews, times(1)).addPublicInfo(any(String.class));
        }

    private void loaderLoadsNews()
        {
        newsLoader.loadNews();
        }

    @Test
    public void givenOneNotSubscriptionNews_whenLoaderLoadsNews_thenLoaderDeliverContentToPublicInfoFun()
        {
        //given
        String newsContent = "123";
        addSomeNewsToLoader(newsContent, SubsciptionType.NONE);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        //when
        loaderLoadsNews();

        //then
        verify(publishableNews).addPublicInfo(stringArgumentCaptor.capture());
        Assert.assertThat(stringArgumentCaptor.getValue(), is(newsContent));
        }

    @Test
    public void givenOneASubscriptionNews_whenLoaderLoadsNews_thenLoaderDeliverContentToAddForSubscriptionFun()
        {
        //given
        String newsContent = "123";
        SubsciptionType subsciptionType = SubsciptionType.A;

        addSomeNewsToLoader(newsContent, subsciptionType);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        //when
        loaderLoadsNews();

        //then
        verify(publishableNews).addForSubscription(stringArgumentCaptor.capture(), any());
        Assert.assertThat(stringArgumentCaptor.getValue(), is(newsContent));
        }

    @Test
    public void givenTwoASubscriptionNews_whenLoaderLoadsNews_thenLoaderDeliverContentToAddForSubscriptionFun()
        {
        //given
        String newsContent = "content1";
        SubsciptionType subsciptionType = SubsciptionType.A;
        addSomeNewsToLoader(newsContent, subsciptionType);

        String newsContent2 = "content2";
        addSomeNewsToLoader(newsContent2, subsciptionType);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        //when
        loaderLoadsNews();

        //then
        verify(publishableNews,times(2)).addForSubscription(stringArgumentCaptor.capture(), any());

        Assert.assertThat(stringArgumentCaptor.getAllValues().get(0), is(newsContent));
        Assert.assertThat(stringArgumentCaptor.getAllValues().get(1), is(newsContent2));
        }

    @Test
    public void givenEmptyNewsList_whenLoaderLoadsNews_thenLoaderDoesNotStartFunction()
        {
        //given


        //when
        loaderLoadsNews();

        //then
        verify(publishableNews, times(0)).addForSubscription(any(), any());
        verify(publishableNews, times(0)).addPublicInfo(any());
        }

    private void addSomeNewsToLoader(SubsciptionType none)
        {
        incomingNews.add(new IncomingInfo("subsctiption", none));
        }

    private void addSomeNewsToLoader(String content, SubsciptionType none)
        {
        incomingNews.add(new IncomingInfo(content, none));
        }

    private void prepareStaticClass()
        {
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        mockStatic(ConfigurationLoader.class);
        mockStatic(PublishableNews.class);
        }

    private void setDefaultValue()
        {
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(NewsReaderFactory.getReader(any())).thenReturn(newsReader);
        when(newsReader.read()).thenReturn(incomingNews);
        when(PublishableNews.create()).thenReturn(publishableNews);
        }
    }
