import { Routes, Route, Navigate } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import CadastroPage from './pages/CadastroPage'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/livros" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/cadastro" element={<CadastroPage />} />
    </Routes>
  )
}
