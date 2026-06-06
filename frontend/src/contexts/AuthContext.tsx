import { createContext, useContext, useState, useCallback, type ReactNode } from 'react';
import { post, get } from '../api';

interface Usuario {
  id: number;
  nome: string;
  email: string;
  admin: boolean;
}

interface AuthContextType {
  usuario: Usuario | null;
  login: (email: string, senha: string) => Promise<void>;
  logout: () => Promise<void>;
  setUsuario: (u: Usuario | null) => void;
}

const STORAGE_KEY = 'usuario';

function salvarUsuario(u: Usuario | null) {
  if (u) sessionStorage.setItem(STORAGE_KEY, JSON.stringify(u));
  else sessionStorage.removeItem(STORAGE_KEY);
}

function carregarUsuario(): Usuario | null {
  try {
    const raw = sessionStorage.getItem(STORAGE_KEY);
    return raw ? (JSON.parse(raw) as Usuario) : null;
  } catch {
    return null;
  }
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuarioState] = useState<Usuario | null>(carregarUsuario);

  const setUsuario = useCallback((u: Usuario | null) => {
    salvarUsuario(u);
    setUsuarioState(u);
  }, []);

  const login = useCallback(async (email: string, senha: string) => {
    const data = await post<Usuario>('/login', { email, senha });
    setUsuario(data);
  }, [setUsuario]);

  const logout = useCallback(async () => {
    await get('/logout');
    setUsuario(null);
  }, [setUsuario]);

  return (
    <AuthContext.Provider value={{ usuario, login, logout, setUsuario }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth deve ser usado dentro de AuthProvider');
  return ctx;
}
