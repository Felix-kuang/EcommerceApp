"use client";

import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import { Pagination, Navigation, Autoplay } from "swiper/modules";

const slides = [
  {
    id: 1,
    image: "https://plus.unsplash.com/premium_photo-1672883552013-506440b2f11c?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    title: "Big Sale!",
    subtitle: "Up to 50% off on selected items!",
  },
  {
    id: 2,
    image: "https://images.unsplash.com/photo-1537832816519-689ad163238b?q=80&w=2059&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    title: "New Arrivals",
    subtitle: "Check out the latest trends now!",
  },
  {
    id: 3,
    image: "https://plus.unsplash.com/premium_photo-1683836722388-8643468c327d?q=80&w=2012&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    title: "Tech Deals",
    subtitle: "Best gadgets at the best prices!",
  },
];

const HomepageSlider = () => {
  return (
    <div className="w-full max-w-screen-xl mx-auto">
      <Swiper
        modules={[Pagination, Navigation, Autoplay]}
        pagination={{ clickable: true }}
        navigation
        autoplay={{ delay: 3000 }}
        loop
        className="rounded-lg shadow-lg"
      >
        {slides.map((slide) => (
          <SwiperSlide key={slide.id}>
            <div className="h-[300px] md:h-[400px] bg-cover bg-center flex flex-col items-center justify-center text-white text-center p-4"
                style={{backgroundImage:`url(${slide.image})`}}
            >
              <h2 className="text-3xl md:text-5xl font-bold drop-shadow-lg mix-blend-difference">
                {slide.title}
              </h2>
              <p className="text-lg md:text-2xl drop-shadow-md mix-blend-difference">
                {slide.subtitle}
              </p>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
};

export default HomepageSlider;
