/**
 * AgriConnect Authentication Module
 * Global auth helpers for JWT token management and route guarding
 */

const TOKEN_KEY = 'agriconnect_token';
const API_BASE_URL = (window.APP_CONFIG && window.APP_CONFIG.apiBaseUrl) || '';
const WEATHER_BASE_URL = (window.APP_CONFIG && window.APP_CONFIG.weatherBaseUrl) || '';

/**
 * Retrieve JWT token from localStorage
 */
function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

/**
 * Save JWT token in localStorage
 */
function setToken(token) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token);
  }
}

/**
 * Remove token from localStorage
 */
function removeToken() {
  localStorage.removeItem(TOKEN_KEY);
}

/**
 * Check if user has an active session
 */
function isLoggedIn() {
  const token = getToken();
  if (!token) return false;
  
  // Check if token is expired
  try {
    const user = getUserFromToken();
    if (user && user.exp) {
      const isExpired = Date.now() >= user.exp * 1000;
      if (isExpired) {
        removeToken();
        return false;
      }
    }
    return true;
  } catch (e) {
    return true; // fallback if no exp field
  }
}

/**
 * Decode JWT token payload without external library
 */
function getUserFromToken() {
  const token = getToken();
  if (!token) return null;

  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;
    
    // Base64URL decode
    let base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    while (base64.length % 4) {
      base64 += '=';
    }
    
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );

    const payload = JSON.parse(jsonPayload);
    
    // Normalize role and name properties
    return {
      ...payload,
      id: payload.userId || payload.id || payload.sub,
      name: payload.name || payload.fullName || payload.sub || 'Farmer',
      email: payload.email || payload.sub,
      role: payload.role || (payload.roles && payload.roles[0]) || (payload.authorities && payload.authorities[0]) || 'FARMER'
    };
  } catch (error) {
    console.error('Error decoding JWT token:', error);
    return null;
  }
}

/**
 * Require authentication - redirect to login page if token is missing
 */
function requireAuth() {
  if (!isLoggedIn()) {
    const currentUrl = encodeURIComponent(window.location.href);
    window.location.href = `login.html?redirect=${currentUrl}`;
  }
}

/**
 * Require ADMIN role - redirect if user is not an administrator
 */
function requireAdmin() {
  requireAuth();
  const user = getUserFromToken();
  const role = user ? (user.role || '').toUpperCase() : '';
  const isAdmin = role === 'ADMIN' || role === 'ROLE_ADMIN';
  
  if (!isAdmin) {
    alert('Access restricted to administrators only.');
    window.location.href = 'dashboard.html';
  }
}

/**
 * Log out user: clears token and redirects to landing page
 */
function logout() {
  removeToken();
  window.location.href = 'index.html';
}

/**
 * Authenticated Fetch wrapper: automatically injects Authorization header
 */
async function authFetch(url, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {})
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(url, {
    ...options,
    headers
  });

  if (response.status === 401 || response.status === 403) {
    console.warn('Unauthorized or token expired, redirecting to login.');
    removeToken();
    window.location.href = 'login.html';
    throw new Error('Session expired. Please log in again.');
  }

  return response;
}

/**
 * UI helper to display toast notifications
 */
function showNotification(message, type = 'success') {
  let toastContainer = document.getElementById('agri-toast-container');
  if (!toastContainer) {
    toastContainer = document.createElement('div');
    toastContainer.id = 'agri-toast-container';
    toastContainer.style.position = 'fixed';
    toastContainer.style.top = '20px';
    toastContainer.style.right = '20px';
    toastContainer.style.zIndex = '9999';
    toastContainer.style.display = 'flex';
    toastContainer.style.flexDirection = 'column';
    toastContainer.style.gap = '10px';
    document.body.appendChild(toastContainer);
  }

  const toast = document.createElement('div');
  const bg = type === 'success' ? '#065f46' : type === 'error' ? '#881337' : '#1e293b';
  const border = type === 'success' ? '#10b981' : type === 'error' ? '#f43f5e' : '#64748b';
  const icon = type === 'success' ? 'fa-check-circle' : type === 'error' ? 'fa-exclamation-circle' : 'fa-info-circle';

  toast.className = 'glass-card animate-slide-up';
  toast.style.cssText = `
    background: ${bg};
    border: 1px solid ${border};
    color: #fff;
    padding: 12px 18px;
    border-radius: 10px;
    box-shadow: 0 10px 25px rgba(0,0,0,0.4);
    min-width: 280px;
    max-width: 420px;
    font-size: 0.9rem;
    display: flex;
    align-items: center;
    gap: 12px;
    transition: opacity 0.3s ease, transform 0.3s ease;
  `;

  toast.innerHTML = `
    <i class="fa-solid ${icon}" style="font-size: 1.2rem;"></i>
    <div style="flex: 1;">${message}</div>
    <button type="button" style="background: none; border: none; color: #fff; opacity: 0.7; cursor: pointer; padding: 0;">
      <i class="fa-solid fa-xmark"></i>
    </button>
  `;

  const closeBtn = toast.querySelector('button');
  closeBtn.onclick = () => {
    toast.style.opacity = '0';
    setTimeout(() => toast.remove(), 300);
  };

  toastContainer.appendChild(toast);

  setTimeout(() => {
    if (toast.parentElement) {
      toast.style.opacity = '0';
      toast.style.transform = 'translateY(-10px)';
      setTimeout(() => toast.remove(), 300);
    }
  }, 4500);
}

// Global exposure
window.getToken = getToken;
window.setToken = setToken;
window.removeToken = removeToken;
window.isLoggedIn = isLoggedIn;
window.getUserFromToken = getUserFromToken;
window.requireAuth = requireAuth;
window.requireAdmin = requireAdmin;
window.logout = logout;
window.authFetch = authFetch;
window.showNotification = showNotification;
window.API_BASE_URL = API_BASE_URL;
window.WEATHER_BASE_URL = WEATHER_BASE_URL;
