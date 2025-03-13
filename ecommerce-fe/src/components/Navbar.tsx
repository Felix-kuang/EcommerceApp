"use client";

import Link from "next/link";
import { ShoppingCart, User} from "lucide-react";
import { useTheme } from "next-themes";
import { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

const Navbar = () => {
  return (
    <nav className="bg-white dark:bg-gray-900 shadow-md p-4 flex justify-between items-center mb-3">
      {/* Logo */}
      <Link href="/" className="text-xl font-bold text-gray-800 dark:text-white">
        MyShop
      </Link>
      
      {/* Search Bar Placeholder */}
      <input
        type="text"
        placeholder="Search products..."
        className="border p-2 rounded-md w-1/3 hidden md:block dark:bg-gray-800 dark:text-white"
      />

      {/* Icons */}
      <div className="flex gap-4 items-center">
        <Link href="/cart">
          <ShoppingCart className="w-6 h-6 text-gray-700 dark:text-white" />
        </Link>
        <Link href="/profile">
          <User className="w-6 h-6 text-gray-700 dark:text-white" />
        </Link>
      </div>
    </nav>
  );
};

export default Navbar;
