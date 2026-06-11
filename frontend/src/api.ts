import * as mockApi from './mockApi'

const MOCK = import.meta.env.VITE_MOCK === 'true'
const BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

const JSON_HEADERS = { 'Content-Type': 'application/json' }

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    credentials: 'include',
    ...options,
  });

  const data = await res.json();

  if (!res.ok) {
    throw { status: res.status, message: data.mensagem ?? 'Erro desconhecido' };
  }

  return data as T;
}

export function get<T>(path: string): Promise<T> {
  if (MOCK) return mockApi.get<T>(path)
  return request<T>(path);
}

export function post<T>(path: string, body: Record<string, unknown>): Promise<T> {
  if (MOCK) return mockApi.post<T>(path, body)
  return request<T>(path, {
    method: 'POST',
    headers: JSON_HEADERS,
    body: JSON.stringify(body),
  });
}

export function put<T>(path: string, body: Record<string, unknown>): Promise<T> {
  if (MOCK) return mockApi.put<T>(path, body)
  return request<T>(path, {
    method: 'PUT',
    headers: JSON_HEADERS,
    body: JSON.stringify(body),
  });
}

export function del<T>(path: string): Promise<T> {
  if (MOCK) return mockApi.del<T>(path)
  return request<T>(path, { method: 'DELETE' });
}
