package edu.iis.mto.staticmock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.reader.NewsReader;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
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


    @Before
    public void setUp() throws Exception
        {
        prepareStaticClass();
        setDefaultValue();
        }

    @Test
    public void simpleAddedOneASubscription()
        {
        //given
        incomingNews.add(new IncomingInfo("subsctiption", SubsciptionType.A));

        //when
        NewsLoader newsLoader=new NewsLoader();
        newsLoader.loadNews();

        //when
        verify(publishableNews, times(1)).addForSubscription(any(String.class),any());

        }

    @Test
    public void simpleAddedOneBSubscriptionAndLoadedTwoTimes()
        {
        //given
        incomingNews.add(new IncomingInfo("subsctiption", SubsciptionType.B));

        //when
        NewsLoader newsLoader = new NewsLoader();
        newsLoader.loadNews();
        newsLoader.loadNews();

        //then
        verify(publishableNews, times(2)).addForSubscription(any(String.class), any());

        }

    @Test
    public void simpleAddedOneCSubscriptionAndLoadedTwoTimes()
        {
        //given
        incomingNews.add(new IncomingInfo("subsctiption", SubsciptionType.C));

        //when
        NewsLoader newsLoader = new NewsLoader();
        newsLoader.loadNews();
        newsLoader.loadNews();

        //then
        verify(publishableNews, times(2)).addForSubscription(any(String.class), any());

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
