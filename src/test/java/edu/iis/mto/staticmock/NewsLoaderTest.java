package edu.iis.mto.staticmock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.reader.NewsReader;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest
    {
    @Mock
    ConfigurationLoader configurationLoader;
    @Mock
    NewsReader newsReader;

    Configuration configuration=new Configuration();
    IncomingNews incomingNews= new IncomingNews();


    @Before
    public void setUp() throws Exception
        {
        prepareStaticClass();
        setDefaultValue();
        }

    @Test
    public void loadNews()
        {



        }

    private void prepareStaticClass()
        {
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        mockStatic(ConfigurationLoader.class);
        }

    private void setDefaultValue()
        {
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(NewsReaderFactory.getReader(any())).thenReturn(newsReader);
        when(newsReader.read()).thenReturn(incomingNews);
        }
    }
