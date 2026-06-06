import { Navigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import type { ReactNode } from 'react'

interface Props {
  children: ReactNode
  adminOnly?: boolean
}

export default function ProtectedRoute({ children, adminOnly = false }: Props) {
  const { usuario } = useAuth()

  if (!usuario) return <Navigate to="/login" replace />
  if (adminOnly && !usuario.admin) return <Navigate to="/livros" replace />

  return <>{children}</>
}
