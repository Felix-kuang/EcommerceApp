import Image from "next/image";
import HomepageSlider from "@/components/HomepageSlider";
import RecommendedProducts from "@/components/RecommendedProducts";

export default function Home() {
  return (
    <div>
      <HomepageSlider />
      <RecommendedProducts />
    </div>
  );
}
