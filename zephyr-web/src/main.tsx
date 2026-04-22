import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'

async function prepareApp() {
    if (import.meta.env.NODE_ENV === 'development') {
        const { worker } = await import('./_mock/browser');
        return worker.start({ onUnhandledRequest: 'bypass' });
    }
}

prepareApp().then(() => {
    createRoot(document.getElementById('root')!).render(
        <StrictMode>
            <App />
        </StrictMode>,
    )
});
