import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./styles/global.css";
import AppLayout from "./layout.tsx";
import { createBrowserRouter, RouterProvider } from "react-router";
import HomePage from "./pages/home.tsx";
import UserPage from "./pages/user.tsx";
import BlogPage from "./pages/blog.tsx";

let router = createBrowserRouter([
  {
    path: "/",
    Component: AppLayout,
    children: [
      {
        index: true,
        Component: HomePage,
      },
      {
        path: "/users",
        Component: UserPage,
      },
      {
        path: "/blogs",
        Component: BlogPage,
      },
    ],
  },
]);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
);
