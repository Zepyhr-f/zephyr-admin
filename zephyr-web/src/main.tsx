import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'

async function prepareApp() {
    try {
        const isDevelopment = import.meta.env.DEV;
        const isMockEnabled = import.meta.env.VITE_ENABLE_MOCK === 'true';

        if (isDevelopment && isMockEnabled) {
            const { worker } = await import('./_mock/browser');
            // 启动 worker，但不阻塞主应用渲染太久
            await worker.start({ 
                onUnhandledRequest: 'bypass',
                serviceWorker: {
                    url: '/mockServiceWorker.js' // 确保路径正确
                }
            });
        }
    } catch (error) {
        console.error('MSW initialization failed:', error);
    }
    return Promise.resolve();
}

prepareApp().then(() => {
    createRoot(document.getElementById('root')!).render(
        <StrictMode>
            <App />
        </StrictMode>,
    )
});
